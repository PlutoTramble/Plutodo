using System.Security.Authentication;
using Microsoft.EntityFrameworkCore;
using Plu_Todo_API_Server.Context;
using Plu_Todo_API_Server.Entities;
using Plu_Todo_API_Server.Entities.Interfaces;
using Plu_Todo_API_Server.Utilities;

namespace Plu_Todo_API_Server.Services;

public class GenericService<T> where T : class, IBasicEntity
{
    protected readonly TodoContext _context;

    public GenericService(TodoContext context)
    {
        _context = context;
    }

    public async Task<IEnumerable<T>> RetrieveAllAsync(User user) 
        => await _context.Set<T>().Where(t => t.Owner == user).ToListAsync();
    
    
    public async Task<T> AddAsync(T entity, User user)
    {
        // Add current user to entity
        entity.Owner = user;
        
        // Deal with ordering
        entity.Ordering = await _context.Set<T>().CountAsync() + 1;

        _context.Set<T>().Add(entity);
        await _context.SaveChangesAsync();

        return entity;
    }
    
    public async Task<T> EditAsync(int id, T entity, User user)
    {
        //Check if user doesn't own it
        if (!_context.Set<T>().Any(t => t.Id == id && t.Owner == user))
            throw new AuthenticationException(MessageUtility.Out(OutputMessages.UserHasNoAuthorization));
        
        // Transaction
        _context.Entry(entity).State = EntityState.Modified;
        
        // var local = _context.Set<T>()
        //     .Local
        //     .FirstOrDefault(entry => entry.Id.Equals(id));
        //
        // if (local != null)
        //     _context.Entry(local).State = EntityState.Detached;
        
        await _context.SaveChangesAsync();
        
        return entity; // That is now the updated Category
    }
    
    public async Task DeleteByIdAsync(int id, User user)
    {
        // Get Entity
        T? entity = await FindByIdAsync(id);
        if (entity == null)
            throw new ArgumentException(MessageUtility.Out(OutputMessages.CouldNotGetEntity));
        
        // Check if entity belongs to User
        if (entity.Owner != user)
            throw new UnauthorizedAccessException(MessageUtility.Out(OutputMessages.UserHasNoAuthorization));
        
        // If it deletes a category, move category's todos' category to null
        if (entity.GetType() == typeof(Category))
            (entity as Category)!.Todos.ForEach(t => t.Category = null);

        // Delete Entity
        _context.Set<T>().Remove(entity);
        await _context.SaveChangesAsync();
    }
    
    public async Task<T?> FindByNameAsync(string entityName, User user)
    {
        return await _context.Set<T>()
            .Where(t => t.Name.ToLower() == entityName.ToLower() && t.Owner == user)
            .FirstOrDefaultAsync();
    }
    
    public async Task<T?> FindByIdAsync(int Id) => 
        await _context.Set<T>().FindAsync(Id);
    
    public bool IsEmpty => _context == null;
}
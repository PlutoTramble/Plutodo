using System.Globalization;
using Plu_Todo_API_Server.Context;
using Plu_Todo_API_Server.Entities;
using Plu_Todo_API_Server.Utilities;

namespace Plu_Todo_API_Server.Services;

public class TodosService : GenericService<Todo>
{
    public TodosService(TodoContext context) : base(context)
    {
    }

    public IEnumerable<Todo> RetrieveAllFromCategory(int categoryId, User user)
    {
        Category? category = user.Categories.FirstOrDefault(c => c.Id == categoryId);

        if (category == null)
            throw new ArgumentException(MessageUtility.Out(OutputMessages.CategoryIdNotFound));
        if (category.Todos == null)
            throw new ArgumentException(MessageUtility.Out(OutputMessages.NoTodosInCategory));

        return category.Todos.OrderBy(t => t.Ordering);
    }

    public IEnumerable<Todo> RetrieveAllOrderByDate(User user) => 
        user.Todos
            .Where(t => t.DateDue != null)
            .OrderBy(t => 
                DateTime.ParseExact(t.DateDue!, "yyyy-MM-dd HH:mm:ss", null, DateTimeStyles.None));

    public IEnumerable<Todo> RetrieveAllFinished(User user) =>
        user.Todos.Where(t => t.Finished);

    public async Task<Todo> AddAsync(Todo todo, int categoryId, User user)
    {
        // Add current user to entity
        todo.Owner = user;
        
        // Add date to entity
        todo.DateCreated = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
        
        // Get Category
        Category? category = await _context.Categories.FindAsync(categoryId);
        
        if (category == null)
            throw new ArgumentException(MessageUtility.Out(OutputMessages.CategoryIdNotFound));
        
        //Checking if category is owned by user
        if (category.Owner != user)
            throw new UnauthorizedAccessException(MessageUtility.Out(OutputMessages.UserHasNoAuthorization));
        
        // Add entity to Category
        todo.Category = category;

        // Deal with ordering
        todo.Ordering = todo.Category.Todos.Count + 1;

        _context.Todos.Add(todo);
        await _context.SaveChangesAsync();

        return todo;
    }
}
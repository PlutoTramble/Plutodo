using System.Security.Claims;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Plu_Todo_API_Server.Entities;
using Plu_Todo_API_Server.Entities.Interfaces;
using Plu_Todo_API_Server.Services;
using Plu_Todo_API_Server.Utilities;

namespace Plu_Todo_API_Server.Controllers;

public class GenericController<T> : ControllerBase where T : class, IBasicEntity
{
    protected readonly GenericService<T> _genericService;
    protected readonly UsersService _usersService;

    public GenericController(GenericService<T> genericService, UsersService usersService )
    {
        _genericService = genericService;
        _usersService = usersService;
    }
    
    [HttpGet]
    public async Task<ActionResult<IEnumerable<T>>> GetAll()
    {
        User? user = await _usersService.GetUserIdAsync(User.FindFirstValue(ClaimTypes.NameIdentifier));

        if (user == null || _genericService.IsEmpty)
            return NotFound();

        // TODO : Check if error
        return Ok(await _genericService.RetrieveAllAsync(user));
    }
    
    [HttpPost]
    public async Task<ActionResult<T>> New(T entity)
    {
        User? user = await _usersService.GetUserIdAsync(User.FindFirstValue(ClaimTypes.NameIdentifier));

        if (user == null || _genericService.IsEmpty)
            return NotFound();
        
        try
        {
            // Returns the newly created entity
            return await _genericService.AddAsync(entity, user);
        }
        catch (ArgumentException e)
        {
            return StatusCode(StatusCodes.Status400BadRequest, new {e.Message});
        }
    }
    
    [HttpPut("{id}")]
    public async Task<ActionResult<T>> Edit(int? id, T entity)
    {
        // Check conditions
        if (id == null)
            return BadRequest(new {Message = MessageUtility.Out(OutputMessages.IdIsNotProvided) });
        
        User? user = await _usersService.GetUserIdAsync(User.FindFirstValue(ClaimTypes.NameIdentifier));

        if (user == null || _genericService.IsEmpty)
            return NotFound();

        // Edit category
        try
        {
            return await _genericService.EditAsync((int)id, entity, user);
        }
        catch (DbUpdateConcurrencyException)
        {
            return StatusCode(StatusCodes.Status500InternalServerError,
                new { Message = MessageUtility.Out(OutputMessages.ErrorWithDatabase) });
        }
        catch (ArgumentException e)
        {
            return StatusCode(StatusCodes.Status400BadRequest, e.Message);
        }
    }
    
    [HttpDelete("{id}")]
    public async Task<ActionResult> Delete(int id)
    {
        // Check if conditions true
        if (id == null)
            return BadRequest(new { Message = MessageUtility.Out(OutputMessages.IdIsNotProvided) });
        
        User? user = await _usersService.GetUserIdAsync(User.FindFirstValue(ClaimTypes.NameIdentifier));

        if (user == null || _genericService.IsEmpty)
            return NotFound();
        
        // Deleting
        try
        {
            await _genericService.DeleteByIdAsync(id, user);
            return Ok(new { Message = MessageUtility.Out(OutputMessages.DeleteSuccess) });
        }
        catch (ArgumentException e)
        {
            return BadRequest(e.Message);
        }
        catch (UnauthorizedAccessException e)
        {
            return Unauthorized(e.Message);
        }
    }
}
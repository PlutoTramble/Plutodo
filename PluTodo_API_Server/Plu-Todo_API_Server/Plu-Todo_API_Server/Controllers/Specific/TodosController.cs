using System.Security.Claims;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Plu_Todo_API_Server.Entities;
using Plu_Todo_API_Server.Services;

namespace Plu_Todo_API_Server.Controllers;

[Route("api/[controller]/[action]")]
[Authorize(AuthenticationSchemes = CookieAuthenticationDefaults.AuthenticationScheme)]
[ApiController]
public class TodosController : GenericController<Todo>
{
    protected readonly TodosService _todosService;
    
    public TodosController(TodosService todosService, UsersService usersService) 
        : base(todosService, usersService)
    {
        _todosService = todosService;
    }

    [HttpGet("{categoryId}")]
    public async Task<ActionResult<IEnumerable<Todo>>> GetFromCategory(int categoryId)
    {
        User? user = await _usersService.GetUserIdAsync(User.FindFirstValue(ClaimTypes.NameIdentifier));
        
        if (user == null || _genericService.IsEmpty)
            return NotFound();
        
        try
        {
            return Ok(_todosService.RetrieveAllFromCategory(categoryId, user));
        }
        catch (ArgumentException e)
        {
            return NotFound(e.Message);
        }
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<Todo>>> GetOrderByDate()
    {
        User? user = await _usersService.GetUserIdAsync(User.FindFirstValue(ClaimTypes.NameIdentifier));
        
        if (user == null || _genericService.IsEmpty)
            return NotFound();

        return Ok(_todosService.RetrieveAllOrderByDate(user));
    }
    
    [HttpGet]
    public async Task<ActionResult<IEnumerable<Todo>>> GetFinished()
    {
        User? user = await _usersService.GetUserIdAsync(User.FindFirstValue(ClaimTypes.NameIdentifier));
        
        if (user == null || _genericService.IsEmpty)
            return NotFound();

        return Ok(_todosService.RetrieveAllFinished(user));
    }
    
    /// <summary>
    /// If TodoEntity doesn't use categoryId, the todoEntity will be created without category.
    /// </summary>
    /// <param name="categoryId"></param>
    /// <param name="todo"></param>
    /// <returns></returns>
    [HttpPost("{categoryId}")]
    public async Task<ActionResult<Todo>> New(int categoryId, Todo todo)
    {
        User? user = await _usersService.GetUserIdAsync(User.FindFirstValue(ClaimTypes.NameIdentifier));

        if (user == null || _genericService.IsEmpty)
            return NotFound();
        
        try
        {
            // Returns the newly created entity
            return await _todosService.AddAsync(todo, categoryId, user);
        }
        catch (ArgumentException e)
        {
            return StatusCode(StatusCodes.Status400BadRequest, new {e.Message});
        }
    }
}
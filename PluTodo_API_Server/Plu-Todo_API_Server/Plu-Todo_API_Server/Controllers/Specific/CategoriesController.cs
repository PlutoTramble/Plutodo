using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Plu_Todo_API_Server.Entities;
using Plu_Todo_API_Server.Services;


namespace Plu_Todo_API_Server.Controllers;

[Route("api/[controller]/[action]")]
[Authorize(AuthenticationSchemes = CookieAuthenticationDefaults.AuthenticationScheme)]
[ApiController]
public class CategoriesController : GenericController<Category>
{
    public CategoriesController(GenericService<Category> categoriesService, UsersService usersService) : base(categoriesService, usersService)
    {
    }

}
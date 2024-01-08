using System.IdentityModel.Tokens.Jwt;
using System.Net;
using System.Security.Authentication;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Net.Http;
using Plu_Todo_API_Server.Entities;
using Plu_Todo_API_Server.Entities.DTOs;
using Plu_Todo_API_Server.Services;
using Plu_Todo_API_Server.Utilities;
using SignInResult = Microsoft.AspNetCore.Identity.SignInResult;

namespace Plu_Todo_API_Server.Controllers;

[Route("api/[controller]/[action]")]
[ApiController]
public class UsersController : ControllerBase
{
    private readonly UsersService _usersService;
    private readonly SignInManager<User> _signInManager;
    
    public UsersController(UsersService usersService, SignInManager<User> signInManager)
    {
        _usersService = usersService;
        _signInManager = signInManager;
    }

    [HttpPost]
    public async Task<ActionResult> Register(RegisterDTO registerDto)
    {
        // Check if password is the same
        if (registerDto.Password != registerDto.PasswordConfirm)
            return StatusCode(StatusCodes.Status400BadRequest, 
                new { Message = MessageUtility.Out(OutputMessages.TwoPasswordsNotEqual) });

        IdentityResult identityResult = await _usersService.RegisterUserAsync(registerDto);
        
        // Check if identityResult has error
        if (identityResult.Errors.Count() >= 1)
        {
            string errorString = "";

            foreach (var error in identityResult.Errors)
                errorString += error.Description + "\n";
            
            return StatusCode(StatusCodes.Status400BadRequest, new {Message = errorString});
        }

        // If unknown error. Internal?
        if (!identityResult.Succeeded)
            return StatusCode(StatusCodes.Status500InternalServerError, 
                new { Message = MessageUtility.Out(OutputMessages.Unknown) });


        LoginDTO login = new LoginDTO()
        {
            Password = registerDto.Password,
            Username = registerDto.Username
        };
        try
        {
            await _usersService.ConnectUserAsync(login, HttpContext);
        }
        catch (AuthenticationException e)
        {
            return StatusCode(StatusCodes.Status400BadRequest, new { Message = e.Message });
        }

        return Ok();
    }

    
    [HttpPost]
    public async Task<ActionResult> Login(LoginDTO loginDto)
    {
        try
        {
            await _usersService.ConnectUserAsync(loginDto, HttpContext);
        }
        catch (AuthenticationException e)
        {
            return StatusCode(StatusCodes.Status400BadRequest, new { Message = e.Message });
        }

        return Ok();
    }
    
    [HttpPost]
    public async Task<ActionResult> Logout()
    {
        await _signInManager.SignOutAsync();
        return Ok();
    }
}
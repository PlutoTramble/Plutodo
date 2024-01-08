using System.Security.Authentication;
using System.Security.Claims;
using System.Text;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using Plu_Todo_API_Server.Context;
using Plu_Todo_API_Server.Entities;
using Plu_Todo_API_Server.Entities.DTOs;
using Plu_Todo_API_Server.Utilities;

namespace Plu_Todo_API_Server.Services;

public class UsersService
{
    private readonly UserManager<User> _userManager;
    private readonly TodoContext _context;

    public UsersService(UserManager<User> userManager, TodoContext context)
    {
        _userManager = userManager;
        _context = context;
    }
    
    public async Task<IdentityResult> RegisterUserAsync(RegisterDTO register)
    {
        User user = new User()
        {
            Email = register.Email,
            UserName = register.Username,
        };
        IdentityResult identityResult = await _userManager.CreateAsync(user, register.Password);
        return identityResult;
    }

    
    public async Task ConnectUserAsync(LoginDTO loginDto, HttpContext httpContext)
    {
        User user = await _userManager.FindByNameAsync(loginDto.Username);
        
        // Check if correct login
        if (user == null || !(await _userManager.CheckPasswordAsync(user, loginDto.Password)))
            throw new AuthenticationException(MessageUtility.Out(OutputMessages.IncorrectUsernameOrPassword));
        
        IList<string> roles = await _userManager.GetRolesAsync(user);
        List<Claim> claims = new List<Claim>();
        
        foreach(string role in roles)
            claims.Add(new Claim(ClaimTypes.Role, role));
        claims.Add(new Claim(ClaimTypes.NameIdentifier, user.Id));
        
        var claimsIdentity = new ClaimsIdentity(
            claims, CookieAuthenticationDefaults.AuthenticationScheme);

        var authProperties = new AuthenticationProperties
        {
            ExpiresUtc = DateTimeOffset.UtcNow.AddDays(10),
            IsPersistent = true,
        };

        await AuthenticationHttpContextExtensions.SignInAsync(
            httpContext,
            CookieAuthenticationDefaults.AuthenticationScheme, 
            new ClaimsPrincipal(claimsIdentity), 
            authProperties);
    }
    
    
    public async Task<User?> GetUserIdAsync(string id) => 
        await _context.Users.FindAsync(id);

    public async Task<User?> GetUserByNameAsync(string name) => 
        await _context.Users.Where(u => u.UserName == name).FirstOrDefaultAsync();
}
using Microsoft.AspNetCore.Identity;

namespace Plu_Todo_API_Server.Entities;

public class User : IdentityUser
{
    public virtual List<Category> Categories { get; set; } = new();

    public virtual List<Todo> Todos { get; set; } = new();
}
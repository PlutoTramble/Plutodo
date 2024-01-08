using System.ComponentModel.DataAnnotations;

namespace Plu_Todo_API_Server.Entities.DTOs;

public class LoginDTO
{
    [Required]
    public string Username { get; set; }

    [Required]
    public string Password { get; set; }
}
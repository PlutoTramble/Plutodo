using System.ComponentModel.DataAnnotations;

namespace Plu_Todo_API_Server.Entities.DTOs;

public class RegisterDTO
{
    [Required]
    public string Username { get; set; }

    [Required]
    [EmailAddress]
    public string Email { get; set; }

    [Required]
    public string Password { get; set; }

    [Required]
    public string PasswordConfirm { get; set; }
}
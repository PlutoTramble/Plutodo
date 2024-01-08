using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;
using Plu_Todo_API_Server.Entities.Interfaces;

namespace Plu_Todo_API_Server.Entities;

public class Todo : IBasicEntity
{
    [Key]
    public int Id { get; set; }
    
    [Required(ErrorMessage = "The title is required")]
    [StringLength(100, ErrorMessage = "The title cannot be longer than 100 characters.")]
    public string Name { get; set; }

    [StringLength(2048, ErrorMessage = "The description cannot be longer than 2048 characters.")]
    public string? Description { get; set; }

    [Required]
    public bool Finished { get; set; }

    public PriorityLevel? Priority { get; set; }
    
    public string? DateCreated { get; set; }

    public string? DateDue { get; set; }
    
    public int Ordering { get; set; }

    // Can be null but has to be setup when adding
    [JsonIgnore]
    public virtual User? Owner { get; set; }

    // To be able to change category
    [ForeignKey("Category")]
    public int CategoryId { get; set; }

    [JsonIgnore]
    public virtual Category? Category { get; set; }
}

public enum PriorityLevel
{
    VeryLow,
    Low,
    Medium,
    High,
    VeryHigh
}
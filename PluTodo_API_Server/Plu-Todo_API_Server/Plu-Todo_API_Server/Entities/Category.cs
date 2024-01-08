using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;
using Plu_Todo_API_Server.Entities.Interfaces;

namespace Plu_Todo_API_Server.Entities
{
    public class Category : IBasicEntity
    {
        [Key]
        public int Id { get; set; }

        [Required(ErrorMessage = "Category's name is required.")]
        [StringLength(100, ErrorMessage = "Category's name cannot surpass 100 characters")]
        public string Name { get; set; }
        
        public int Ordering { get; set; }

        [JsonIgnore] 
        public virtual List<Todo> Todos { get; set; } = new();
        
        // Can be null but has to be setup when adding
        [JsonIgnore]
        public virtual User? Owner { get; set; }
    }
}

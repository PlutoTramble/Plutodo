namespace Plu_Todo_API_Server.Entities.Interfaces;

public interface IBasicEntity
{
    public int Id { get; set; }
    
    public string Name { get; set; }
    
    public int Ordering { get; set; }

    public User Owner { get; set; }
}
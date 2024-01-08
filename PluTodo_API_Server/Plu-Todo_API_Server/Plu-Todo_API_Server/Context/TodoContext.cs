using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using Plu_Todo_API_Server.Entities;

namespace Plu_Todo_API_Server.Context;

public class TodoContext : IdentityDbContext<User>
{
    public TodoContext(DbContextOptions<TodoContext> options) : base(options)
    {
    }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);
    }
    
    public DbSet<Category> Categories { get; set; }
    public DbSet<Todo> Todos { get; set; }
}
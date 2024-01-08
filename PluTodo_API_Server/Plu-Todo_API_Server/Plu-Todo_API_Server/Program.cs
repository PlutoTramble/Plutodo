using System.Text;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using Plu_Todo_API_Server.Context;
using Plu_Todo_API_Server.Entities;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Plu_Todo_API_Server.Services;


var builder = WebApplication.CreateBuilder(args);

builder.Services.AddDbContext<TodoContext>(options =>
{
    options.UseNpgsql(builder.Configuration.GetConnectionString("docker"));
    options.UseLazyLoadingProxies();
});
builder.Services.AddIdentity<User, IdentityRole>().AddEntityFrameworkStores<TodoContext>();

// Add services to the container.
builder.Services.AddCors(o =>
{
    o.AddPolicy("Allow all", policy =>
    {
        policy.WithOrigins("https://tramble.ddns.net:4200", "http://localhost");
        policy.AllowAnyMethod();
        policy.AllowAnyHeader();

        policy.AllowCredentials();
    });
});

// Scopes
builder.Services.AddScoped<UsersService>();
builder.Services.AddScoped<GenericService<Category>>();
builder.Services.AddScoped<TodosService>();

builder.Services.AddControllers();

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.ConfigureApplicationCookie(options =>
{
    options.Cookie.HttpOnly = false;
});

builder.Services.AddAuthentication(CookieAuthenticationDefaults.AuthenticationScheme)
    .AddCookie(CookieAuthenticationDefaults.AuthenticationScheme);

builder.Services.AddHttpContextAccessor();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseSwaggerUI(options =>
{
    options.SwaggerEndpoint("/swagger/v1/swagger.json", "v1");
    options.RoutePrefix = string.Empty;
});

app.UseRouting();

app.UseCors("Allow all");

//app.UseHttpsRedirection();

app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();

app.Run();
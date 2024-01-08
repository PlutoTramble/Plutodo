namespace Plu_Todo_API_Server.Utilities;

public enum OutputMessages
{
    Unknown,
    
    // General
    IdIsNotProvided,
    DeleteSuccess,
    OrderingError, // May be front-end error. Need to contact Dev
    CouldNotGetEntity,
    
    // Authentication
    TwoPasswordsNotEqual,
    IncorrectUsernameOrPassword,
    UserHasNoAuthorization,
    
    // Category
    UserAlreadyHasCategorySameName,
    

    // Database
    ErrorWithDatabase,
    
    //Todos
    CategoryIdNotFound,
    NoTodosInCategory
}
namespace Plu_Todo_API_Server.Utilities;

public static class MessageUtility
{
    public static string Out(OutputMessages value) => Enum.GetName(typeof(OutputMessages), value)!;
}
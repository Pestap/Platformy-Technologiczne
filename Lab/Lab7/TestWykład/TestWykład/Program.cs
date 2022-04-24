// See https://aka.ms/new-console-template for more information
Console.WriteLine("Hello, World!");

List<string> namse = new List<string>() { "Monika", "Karol", "Ewa", "Marta", "Eliza", "Magda" };

var result = from e in namse
             group e by new
             {
                 Women = e[e.Length - 1] == 'a',
                 Length = e.Length
             } into g
             where g.Count() < 5
             orderby g.Key.Length
             select g;


foreach (var group in result)
{
    Console.WriteLine("Women: {0}, Length {1}", group.Key.Women, group.Key.Length);
    foreach(var value in group)
    {
        Console.WriteLine("- {0}", value);
           
    }
}
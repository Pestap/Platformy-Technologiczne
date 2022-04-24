using Lab1;
using System.Runtime.Serialization.Formatters.Binary;

namespace lab1
{
    static class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Folder : " + args[0]);
            string path = args[0];

            ProcessDirectory(path,0);

            Console.WriteLine("Oldest file: {0}", (new DirectoryInfo(path)).GetOldestFile());

            Collect(path);
            PrintSerializedCollection();

        }

        public static void ProcessDirectory(string path, int depth)
        {
            string[] files = Directory.GetFiles(path);
            string[] directories = Directory.GetDirectories(path);

            foreach(string file in files)
            {
                var pathParts = file.Split("\\");
                var fileName = pathParts[pathParts.Length - 1];

                for(int i =0;  i<depth; i++)
                {
                    Console.Write("    ");
                }
                var fileInfo = new FileInfo(file);
                Console.WriteLine("{0} size: {1} bytes {2}", fileName, fileInfo.Length, fileInfo.GetAttributes());

            }
            foreach(string directory in directories)
            {
                var dirParts = directory.Split("\\");
                var dirname = dirParts[dirParts.Length - 1];
                for (int i = 0; i < depth; i++)
                {
                    Console.Write("    ");
                }
                var numberOfThings = Directory.GetFiles(directory).Length + Directory.GetDirectories(directory).Length;
                Console.WriteLine("{0} ({1}) {2}", dirname, numberOfThings,new DirectoryInfo(directory).GetAttributes());
                ProcessDirectory(directory, depth + 1);

            }
        }

        public static string GetAttributes(this FileSystemInfo fileSystemInfo)
        {
            string output = "";
            var fileAttributes = fileSystemInfo.Attributes;
            if((fileAttributes & FileAttributes.ReadOnly) == FileAttributes.ReadOnly)
            {
                output += "r";
            }
            else
            {
                output += "-";
            }

            if ((fileAttributes & FileAttributes.Archive) == FileAttributes.Archive)
            {
                output += "a";
            }
            else
            {
                output += "-";
            }

            if ((fileAttributes & FileAttributes.Hidden) == FileAttributes.Hidden)
            {
                output += "h";
            }
            else
            {
                output += "-";
            }

            if ((fileAttributes & FileAttributes.System) == FileAttributes.System)
            {
                output += "s";
            }
            else
            {
                output += "-";
            }
            return output;
        }


        public static DateTime GetOldestFile(this DirectoryInfo dirInfo)
        {
            DateTime oldest = DateTime.MaxValue;
            

            foreach(FileInfo file in dirInfo.GetFiles())
            {
                DateTime fileCreationTime = File.GetCreationTime(file.FullName);
                if (fileCreationTime < oldest)
                {
                    oldest = File.GetCreationTime(file.FullName);
                }
            }
            foreach(DirectoryInfo directoryInfo in dirInfo.GetDirectories())
            {
                var oldestFromDir = directoryInfo.GetOldestFile();
                if(oldestFromDir < oldest)
                {
                    oldest = oldestFromDir;
                }
            }
            return oldest;
        }

        public static SortedDictionary<String, int> Collect(string path)
        {
            string[] files = Directory.GetFiles(path);
            string[] directories = Directory.GetDirectories(path);
            SortedDictionary<string, int> collection = new SortedDictionary<string, int>(new StringComparator());

            foreach(string file in files)
            {
                var pathParts = file.Split("\\");
                var fileName = pathParts[pathParts.Length - 1];
                var fileInfo = new FileInfo(file);
                collection.Add(fileName, (int)fileInfo.Length);
            }

            foreach (string directory in directories)
            {
                var dirParts = directory.Split("\\");
                var dirname = dirParts[dirParts.Length - 1];
                var numberOfThings = Directory.GetFiles(directory).Length + Directory.GetDirectories(directory).Length;
                collection.Add(dirname, numberOfThings);

            }
            //Serializacja

            FileStream fs = new FileStream("collectionSerialized.dat", FileMode.Create);
            BinaryFormatter formatter = new BinaryFormatter();

            try
            {
                formatter.Serialize(fs, collection);
            }catch(Exception ex)
            {
                Console.WriteLine(ex.ToString() + ": Serialization error!");
            }
            fs.Close();
            return collection;
        }

        public static void PrintSerializedCollection()
        {
            SortedDictionary<String, int> collection = new SortedDictionary<string, int>(new StringComparator());

            FileStream fs = new FileStream("collectionSerialized.dat", FileMode.Open);
            BinaryFormatter formatter = new BinaryFormatter();
            try
            {
                collection = (SortedDictionary<string, int>)formatter.Deserialize(fs);
            }catch(Exception ex)
            {
                Console.WriteLine(ex.ToString + ": Deserialization error!");
            }

            foreach(var element in collection)
            {
                Console.WriteLine("{0} -> {1}", element.Key, element.Value);
            }
        }
    }
}
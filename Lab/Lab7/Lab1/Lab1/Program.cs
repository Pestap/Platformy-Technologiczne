namespace lab1
{
    static class Program
    {
        static void Main(String[] args)
        {
            Console.WriteLine("Folder : " + args[0]);
            String path = args[0];

            processDirectory(path,0);



        }

        public static void processDirectory(String path, int depth)
        {
            String[] files = Directory.GetFiles(path);
            String[] directories = Directory.GetDirectories(path);

            foreach(String file in files)
            {
                var pathParts = file.Split("\\");
                var fileName = pathParts[pathParts.Length - 1];

                for(int i =0;  i<depth; i++)
                {
                    Console.Write("    ");
                }
                var fileInfo = new FileInfo(file);
                Console.WriteLine("{0} size: {1} bytes {2}", fileName, fileInfo.Length, fileInfo.getAttributes());

            }
            foreach(String directory in directories)
            {
                var dirParts = directory.Split("\\");
                var dirname = dirParts[dirParts.Length - 1];
                for (int i = 0; i < depth; i++)
                {
                    Console.Write("    ");
                }
                var numberOfThings = Directory.GetFiles(directory).Length + Directory.GetDirectories(directory).Length;
                Console.WriteLine("{0} ({1}) {2}", dirname, numberOfThings,new DirectoryInfo(directory).getAttributes());
                processDirectory(directory, depth + 1);

            }
        }

        public static String getAttributes(this FileSystemInfo fileSystemInfo)
        {
            String output = "";
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


        public static DateTime getOldestFile(this DirectoryInfo dirInfo)
        {
            
        }
    }
}
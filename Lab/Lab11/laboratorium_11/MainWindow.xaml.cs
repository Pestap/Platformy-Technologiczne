using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Globalization;
using System.IO;
using System.IO.Compression;
using System.Linq;
using System.Net;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Forms;

namespace laboratorium_11
{
    public partial class MainWindow
    {
        private NewtonSymbol newtonSymbol;
        private int highestPercentageReached;
        public MainWindow()
        {
            InitializeComponent();
        }


        private readonly string[] HostNames = { "www.microsoft.com", "www.apple.com",
            "www.google.com", "www.ibm.com", "cisco.netacad.net",
            "www.oracle.com", "www.nokia.com", "www.hp.com", "www.dell.com",
            "www.samsung.com", "www.toshiba.com", "www.siemens.com",
            "www.amazon.com", "www.sony.com", "www.canon.com", "www.alcatel-lucent.com",
            "www.acer.com", "www.motorola.com" };


        //obsługa opcji z klasą TASk
        private void ButtonClick_NewtonSymbolTasks(object sender, RoutedEventArgs e)
        {
            int k, n;
            if (!Int32.TryParse(TextBoxN.Text, out n) || !Int32.TryParse(TextBoxK.Text, out k))
            {
                SetErrorMsg("Ustaw N i K!");
                return;
            }
            newtonSymbol = new NewtonSymbol(n, k);
            double result = newtonSymbol.CalculateTasks();
            //obsługa błędnego wejścia
            switch (result)
            {
                case -1:
                    SetErrorMsg("N i K muszą być dodatnie!");
                    break;
                case -2:
                    SetErrorMsg("N musi być większe lub równe K!");
                    break;
                default:
                    TextBoxTasks.Text = result.ToString(CultureInfo.InvariantCulture);
                    SetErrorMsg("");
                    break;
            }
            
        }

        //obsługa wersji z delegatami
        private void ButtonClick_NewtonSymbolDelegates(object sender, RoutedEventArgs e)
        {
            int k, n;
            if (!Int32.TryParse(TextBoxN.Text, out n) || !Int32.TryParse(TextBoxK.Text, out k))
            {
                SetErrorMsg("Ustaw N i K!");
                return;
            }
            newtonSymbol = new NewtonSymbol(n, k);
            double result = newtonSymbol.CalculateDelegates();
            //obsługa błędnego wejścia
            switch (result)
            {
                case -1:
                    SetErrorMsg("N i K muszą być dodatnie!");
                    break;
                case -2:
                    SetErrorMsg("N musi być większe lub równe K!");
                    break;
                default:
                    TextBoxDelegates.Text = result.ToString(CultureInfo.InvariantCulture);
                    SetErrorMsg("");
                    break;
            }
        }
        private async void ButtonClick_NewtonSymbolAsyncAwait(object sender, RoutedEventArgs e)
        {
            int k, n;
            if (!Int32.TryParse(TextBoxN.Text, out n) || !Int32.TryParse(TextBoxK.Text, out k))
            {
                SetErrorMsg("Ustaw N i K!");
                return;
            }
            newtonSymbol = new NewtonSymbol(n, k);
            double result = await newtonSymbol.CalculateAsyncAwait();
            switch (result)
            {
                case -1:
                    SetErrorMsg("N i K muszą być dodatnie!");
                    break;
                case -2:
                    SetErrorMsg("N musi być większe lub równe K!");
                    break;
                default:
                    TextBoxAsyncAwait.Text = result.ToString(CultureInfo.InvariantCulture);
                    SetErrorMsg("");
                    break;
            }

        }
        private void ButtonClick_GetFib(object sender, RoutedEventArgs e)
        {
            int i;
            if(!Int32.TryParse(TextBoxI.Text, out i))
            {
                SetErrorMsg("Nieprawidłowe i!");
                return;
            }

            if (i <= 0)
            {
                SetErrorMsg("Indeks musi być większy od 0!");
                return;
            }
            BackgroundWorker fibonacciWorker = new BackgroundWorker();
            fibonacciWorker.DoWork += fibonacciWorker_DoWork;
            fibonacciWorker.RunWorkerCompleted += fibonacciWorker_RunWorkerCompleted;
            fibonacciWorker.ProgressChanged += fibonacciWorker_ProgressChanged;
            highestPercentageReached = 0;
            ProgressBar.Value = 0;
            fibonacciWorker.RunWorkerAsync(i);
        }

        private void fibonacciWorker_DoWork(object sender, DoWorkEventArgs e)
        {
            BackgroundWorker worker = sender as BackgroundWorker;
            if (worker != null)
            {
                worker.WorkerReportsProgress = true;
                e.Result = ComputeFibonacci((int) e.Argument, worker, e);
            }
        }

        private void fibonacciWorker_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            TextBoxFibonacci.Text = e.Result.ToString();
        }

        private void fibonacciWorker_ProgressChanged(object sender, ProgressChangedEventArgs e)
        {
            ProgressBar.Value = e.ProgressPercentage;
        }

        private UInt64 ComputeFibonacci(int n, BackgroundWorker worker, DoWorkEventArgs e)
        {
            if(n <= 0)
            {
                SetErrorMsg("Nieprawidłowy argument!");
                return 0;
            }
            UInt64 result = 0;

            if (worker.CancellationPending)
            {
                e.Cancel = true;
            }
            else
            {
                List<UInt64> listOfFibonacciElements = new List<UInt64>();
                
                for(int i = 1; i <= n; i++)
                {
                    if (i <= 2)
                    {
                        listOfFibonacciElements.Add(1);
                    }
                    else
                    {
                        //spamiętywanie (zamiast reskurencji)
                        //pobieramy dwa ostatnie elementy
                        var a = listOfFibonacciElements[listOfFibonacciElements.Count - 1];
                        var b = listOfFibonacciElements[listOfFibonacciElements.Count - 2];
                        listOfFibonacciElements.Add(a + b);

                    }
                    int percentComplete = (int)((float)i / n * 100);
                    if (percentComplete > highestPercentageReached)
                    {
                        highestPercentageReached = percentComplete;
                        worker.ReportProgress(percentComplete);
                        Thread.Sleep(20);
                    }
                }
                result = listOfFibonacciElements.Last(); //ostatni element jest wynikiem
            }
            

            return result;
        }

        private void ButtonClick_ResolveDNS(object sender, RoutedEventArgs e)
        {
            var domainList = ConvertDomains();
            TextBoxOutput.Text = "";
            //wypisujemy wyniki
            foreach (var domain in domainList)
            {
                TextBoxOutput.Text += $"{domain.Item1} => {domain.Item2}\n";
            }

        }
        public List<Tuple<string, string>> ConvertDomains()
        {
            //równoległa pętla
            List<Tuple<string, string>> result = new List<Tuple<string, string>>();
            HostNames.AsParallel()
                .ForAll(host =>
                {
                    lock (result)
                    {
                        result.Add(Tuple.Create(host, Dns.GetHostAddresses(host).First().ToString()));
                    }
                });

            return result;
        }

        private void ButtonClick_Compress(object sender, RoutedEventArgs e)
        {
            var dialog = new FolderBrowserDialog()
            {
                Description = Properties.Resources.MainWindow_ButtonClick_Compress_Select_directory_to_compress
            };
            DialogResult result = dialog.ShowDialog();
            if(result == System.Windows.Forms.DialogResult.OK)
            {
                DirectoryInfo directoryInfo = new DirectoryInfo(dialog.SelectedPath);
                CompressDirectory(directoryInfo);
            }

        }
        private void ButtonClick_Decompress(object sender, RoutedEventArgs e)
        {
            var dialog = new FolderBrowserDialog()
            {
                Description = Properties.Resources.MainWindow_ButtonClick_Decompress_Select_directory_to_decompress
            };
            DialogResult result = dialog.ShowDialog();
            if (result == System.Windows.Forms.DialogResult.OK)
            {
                DirectoryInfo directoryInfo = new DirectoryInfo(dialog.SelectedPath);
                DecompressDirectory(directoryInfo);
            }
        }

        public void CompressDirectory(DirectoryInfo dir)
        {
            List<Task> filesToCompress = new List<Task>();
            //równoległa kompresja 
            foreach (var file in dir.GetFiles())
            {
                filesToCompress.Add(Task.Factory.StartNew(() => CompressFile(file)));
            }

            Task.WaitAll(filesToCompress.ToArray());
            //wyświetlamy wiadomość
            System.Windows.MessageBox.Show("Kompresja zakończona");
        }

        private void CompressFile(FileInfo file)
        {
            using (FileStream originalFileStream = file.OpenRead())
            {
                //sprawdzamy czy plik nie jest hidden lub .gz
                if ((File.GetAttributes(file.FullName) & FileAttributes.Hidden) != FileAttributes.Hidden & file.Extension != ".gz")
                {
                    using (FileStream compressedFileStream = File.Create(file.FullName + ".gz"))
                    {
                        using (GZipStream compressionStream = new GZipStream(compressedFileStream, CompressionMode.Compress))
                        {
                            //kopiujemy dane z pliku to compressionStream
                            originalFileStream.CopyTo(compressionStream);

                        }
                    }
                    FileInfo info = new FileInfo($"{file.Directory.FullName}{Path.DirectorySeparatorChar}{file.Name}.gz");
                }

            }

        }

        public void DecompressDirectory(DirectoryInfo dir)
        {
            List<Task> filesToDecompress = new List<Task>();
            foreach (var file in dir.GetFiles("*.gz"))
            {
                filesToDecompress.Add(Task.Factory.StartNew(() => DecompressFile(file)));
            }
            Task.WaitAll(filesToDecompress.ToArray());
            System.Windows.MessageBox.Show("Dekompresja ukończona.");
        }

        private void DecompressFile(FileInfo file)
        {
            using (FileStream originalFileStream = file.OpenRead())
            {
                string currentFileName = file.FullName;
                string newFileName = currentFileName.Remove(currentFileName.Length - file.Extension.Length);

                using (FileStream decompressedFileStream = File.Create(newFileName))
                {
                    using (GZipStream decompressionStream = new GZipStream(originalFileStream, CompressionMode.Decompress))
                    {
                        decompressionStream.CopyTo(decompressedFileStream);
                    }
                }
            }


        }

        private void ButtonClick_Check(object sender, RoutedEventArgs e)
        {
            Random random = new Random();
            textBox_responsive.Text = random.Next().ToString();
        }
        private void LabelDoubleClick_ClearErrorMsg(object sender, RoutedEventArgs e)
        {
            SetErrorMsg("");
        }

        private void SetErrorMsg(string error)
        {
            LabelError.Content = error;
        }
    }
}

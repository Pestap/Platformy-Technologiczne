using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace Lab2NET
{
    /// <summary>
    /// Logika interakcji dla klasy CreateWindow.xaml
    /// </summary>
    public partial class CreateWindow : Window
    {
        private string path;
        private bool result;
        public CreateWindow(string path)
        {
            this.path = path;
            result = false;
            Trace.WriteLine(path);
            InitializeComponent();
        }

        private void createButton_Click(object sender, RoutedEventArgs e)
        {
            //wczytaujemy dane z formularza
            //
            bool isFile = FileRadio.IsChecked.Value;
            bool isDir = DirRadio.IsChecked.Value;
            bool isReadOnly = ROcheckbox.IsChecked.Value;
            bool isArchive = ACheckbox.IsChecked.Value;
            bool isHidden = HCheckbox.IsChecked.Value;
            bool isSystem = SCheckbox.IsChecked.Value;

            string name = NameTextBox.Text.Trim();

            if (name == "" || (!isFile && !isDir))
            {
                System.Windows.MessageBox.Show("Type or name not specified!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
            else if (isFile && !Regex.IsMatch(name,"^[0-9a-zA-Z_~-]{1,8}\\.(txt|html|php)$"))
            {
                System.Windows.MessageBox.Show("File name contains illegal symbols!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
            else
            {
                //atrybuty
                FileAttributes attr = FileAttributes.Normal;
                path = path + "\\" + name;
                if (isReadOnly)
                {
                    attr |= FileAttributes.ReadOnly;
                }
                if (isArchive)
                {
                    attr |= FileAttributes.Archive;
                }
                if (isHidden)
                {
                    attr |= FileAttributes.Hidden;
                }
                if (isSystem)
                {
                    attr |= FileAttributes.System;
                }


                if (isFile)
                {
                    File.Create(path);
                    System.Windows.MessageBox.Show("File succesfully created", "Succes!", MessageBoxButton.OK, MessageBoxImage.Information);
                }
                else
                {
                    Directory.CreateDirectory(path);
                    System.Windows.MessageBox.Show("Directory succesfully created", "Succes!", MessageBoxButton.OK, MessageBoxImage.Information);
                }
                File.SetAttributes(path, attr);
                result = true;
                Close();
            }


        }

        public bool GetResult()
        {
            return result;
        }

        public string GetPath()
        {
            return path;
        }
        
        private void cancelButton_Click(object sender, RoutedEventArgs e)
        {
            Close();
        }
    }
}

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Forms;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.IO;

namespace Lab2NET
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();

        }
        private void openClick(object sender, RoutedEventArgs e)
        {
            Trace.WriteLine("Open");
            var dlg = new FolderBrowserDialog() { Description = "Wybierz folder" };
            DialogResult result = dlg.ShowDialog();
            
            if(result == System.Windows.Forms.DialogResult.OK)
            {
                treeView.Items.Clear();
                DirectoryInfo dir = new DirectoryInfo(dlg.SelectedPath);
                Trace.WriteLine(dir.FullName);
                treeView.Items.Add(CreateTreeDirectory(dir));
            }
        }
        private void exitClick(object sender, RoutedEventArgs e)
        {
            Close();
        }

        private TreeViewItem CreateTreeDirectory(DirectoryInfo dir)
        {
            var root = new TreeViewItem
            {
                Header = dir.Name,
                Tag = dir.FullName
            };
            root.ContextMenu = new ContextMenu();
            var menuitem1 = new MenuItem { Header = "Create" };
            menuitem1.Click += new RoutedEventHandler(CreateFileOrDir);
            var menuitem2 = new MenuItem { Header = "Delete" };
            menuitem2.Click += new RoutedEventHandler(DeleteClick);
            root.ContextMenu.Items.Add(menuitem1);
            root.ContextMenu.Items.Add(menuitem2);
            root.Selected += new RoutedEventHandler(UpdateStatusBar);
            foreach (DirectoryInfo subdir in dir.GetDirectories())
            {
                root.Items.Add(CreateTreeDirectory(subdir));
            }

            foreach(FileInfo file in dir.GetFiles())
            {
                root.Items.Add(CreateTreeFile(file));   
            }
            Trace.WriteLine(root.ToString());
            return root;
        }

        private TreeViewItem CreateTreeFile(FileInfo file)
        {
            var item = new TreeViewItem
            {
                Header = file.Name,
                Tag = file.FullName
            };
            item.ContextMenu = new ContextMenu();
            var menuitem1 = new MenuItem { Header = "Open" };
            menuitem1.Click += new RoutedEventHandler(OpenFile);
            var menuitem2 = new MenuItem { Header = "Delete" };
            menuitem2.Click += new RoutedEventHandler(DeleteClick);
            item.ContextMenu.Items.Add(menuitem1);
            item.ContextMenu.Items.Add(menuitem2);
            item.Selected += new RoutedEventHandler(UpdateStatusBar);
            return item;
        }

        private void UpdateStatusBar(object sender, RoutedEventArgs e)
        {
            TreeViewItem selectedItem = (TreeViewItem)treeView.SelectedItem;
            if(selectedItem != null)
            {
                FileAttributes selectedFileAttributes = File.GetAttributes((string)selectedItem.Tag);
                string result = "";
                if ((selectedFileAttributes & FileAttributes.ReadOnly) == FileAttributes.ReadOnly)
                {
                    result += "r";
                }
                else
                {
                    result += "-";
                }

                if ((selectedFileAttributes & FileAttributes.Archive) == FileAttributes.Archive)
                {
                    result += "a";
                }
                else
                {
                    result += "-";
                }

                if ((selectedFileAttributes & FileAttributes.Hidden) == FileAttributes.Hidden)
                {
                    result += "h";
                }
                else
                {
                    result += "-";
                }

                if ((selectedFileAttributes & FileAttributes.System) == FileAttributes.System)
                {
                    result += "s";
                }
                else
                {
                    result += "-";
                }

                StatusBarTextBlock.Text = result;
            }
        }

        private void CreateFileOrDir(object sender, RoutedEventArgs e)
        {
            TreeViewItem item = (TreeViewItem)treeView.SelectedItem;
            if (item != null)
            {
                string path = (string)item.Tag;
                CreateWindow createWindow = new CreateWindow(path);
                createWindow.ShowDialog();

                if(createWindow.GetResult() == true)
                {
                    if (File.Exists(createWindow.GetPath()))
                    {
                        FileInfo file = new FileInfo(createWindow.GetPath());
                        item.Items.Add(CreateTreeFile(file));
                    }else if (Directory.Exists(createWindow.GetPath())){
                        DirectoryInfo dir = new DirectoryInfo(createWindow.GetPath());
                        item.Items.Add(CreateTreeDirectory(dir));
                    }
                }
            }

        }

        private void DeleteDir(string path)
        {
           
           DirectoryInfo dirinfo = new DirectoryInfo(path);
           foreach (DirectoryInfo subdir in dirinfo.GetDirectories())
           {
                DeleteDir(subdir.FullName); 
           }
           foreach(FileInfo file in dirinfo.GetFiles())
           {
                File.Delete(file.FullName);
           }
           Directory.Delete(path);
            
        }
 
        private void DeleteClick(object sender, RoutedEventArgs e)
        {
            TreeViewItem item = (TreeViewItem)treeView.SelectedItem;
            if (item != null)
            {
                Trace.WriteLine("Delete file: " + item.ToString());
                string path = (string)item.Tag;

                FileAttributes fileAttributes = File.GetAttributes(path);
                // jeżeli jest ReadOnly to usuwamy ten atrybut
                if ((fileAttributes & FileAttributes.ReadOnly) == FileAttributes.ReadOnly)
                {
                    fileAttributes = fileAttributes & ~FileAttributes.ReadOnly;
                }

                // usuwamy plik lub katalog
                if ((fileAttributes & FileAttributes.Directory) == FileAttributes.Directory)
                {
                    DeleteDir(path);
                }
                else
                {
                    File.Delete(path);
                }

                //usuwamy ze struktury drzewa
                if (item == treeView.Items[0])
                {
                    treeView.Items.Clear();
                }
                else
                {
                    TreeViewItem parent = (TreeViewItem)item.Parent;
                    parent.Items.Remove(item);
                }
            }
        }

        private void OpenFile(object sender, RoutedEventArgs e)
        {
            TreeViewItem item = (TreeViewItem)treeView.SelectedItem;
            if (item != null)
            {
                string name = (string)item.Tag;
                Trace.WriteLine("Open file " + name);
                string fileContent =File.ReadAllText(name);
                scrollViewer.Content = new TextBlock() { Text = fileContent };
            }
        }
    }

}

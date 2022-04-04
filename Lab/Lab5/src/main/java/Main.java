public class Main {
    public static void main(String[] args){
        MageController controller = new MageController(new MageRepository());

        String res1 = controller.save("Gandalf", 10);
        String res2 = controller.save("Merlin", 8);
        String res3 = controller.save("Dumbleadore", 7);
        System.out.println(res1 + ", " + res2 +", " +res3);

        System.out.println(controller.find("Gandalf"));
        System.out.println(controller.delete("Gandalf"));
        System.out.println(controller.find("Gandalf"));
    }
}

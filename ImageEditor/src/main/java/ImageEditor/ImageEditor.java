package ImageEditor;


class ImageEditor{



    ImageEditor(){

    }


    public void run(){

        ImageFile image = new ImageFile(".\\ImageEditor\\assets\\image1.bmp");
        image.readFile();
        image.grayScale();
        image.writeFile();
        
    }

    public static void main(String[] args){
        new ImageEditor().run();
    }
}
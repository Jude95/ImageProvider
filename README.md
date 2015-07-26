# ImageProvider
图片提供者,从系统，相册，网络提供图片。


##依赖
`compile 'com.jude:imageprovider:1.0.3'`

##用法
`ImageProvider provider = new ImageProvider(this);`  
然后调用这几个方法就行了，很简单。  
`void getImageFromCamera(OnImageSelectListener mListener)`  
`void getImageFromAlbum(OnImageSelectListener mListener)`  
`void getImageFromNet(OnImageSelectListener mListener)`  
图片裁剪，传入原始图片URI，输出宽高。  
`void corpImage(Uri uri,int width,int height,OnImageSelectListener listener)`  
OnImageSelectListener有3个回调。建议这样写:

    @Override
    public void onImageSelect() {
        dialog = new ProgressDialog(this);
        dialog.show();
    }

    @Override
    public void onImageLoaded(Uri uri) {
        dialog.dismiss();
        image.setImageURI(uri);
    }

    @Override
    public void onError() {
        Toast.makeText(this,"Load Error",Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

onImageLoaded会回调一个Uri。相机与网络，裁剪都是一个文件Uri。相册是系统数据库的Uri。

# ImageProvider
图片提供者,从系统，相册，网络提供图片。
相册选择部分使用了[MultiImageSelector](https://github.com/lovetuzitong/MultiImageSelector)

##依赖
`compile 'com.jude:imageprovider:3.0.1'`

##用法
`ImageProvider provider = new ImageProvider(this);`

然后调用这几个方法就行了，很简单。  
`void getImageFromCamera(OnImageSelectListener mListener)`   
`void getImageFromAlbum(OnImageSelectListener mListener)`  
`void getImageFromAlbum(OnImageSelectListener mListener,int maxCount)`  
`void getImageFromCameraOrAlbum(OnImageSelectListener mListener)`  
`void getImageFromCameraOrAlbum(OnImageSelectListener mListener,int maxCount)`   
`void getImageFromNet(OnImageSelectListener mListener)`   
图片裁剪，传入原始图片URI，输出宽高。  
`void corpImage(Uri uri,int width,int height,OnImageSelectListener listener)`    
OnImageSelectListener会直接回调:


    @Override
    public void onImageLoaded(Uri uri) {
        image.setImageURI(uri);
    }


onImageLoaded会回调一个文件Uri。  
注意有时你的相机与相册返回的图片过大。不能直接读取所返回的uri。可以使用
`ImageProvider.readImageWithSize(uri,width,height);`以缩放大小加载图片。


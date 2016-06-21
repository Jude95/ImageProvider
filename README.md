# ImageProvider
图片提供者,从系统，相册，网络提供图片。
相册选择部分使用了[MultiImageSelector](https://github.com/lovetuzitong/MultiImageSelector)

##依赖
`compile 'com.jude:imageprovider:2.1.1'`

##用法
`ImageProvider provider = new ImageProvider(this);`
在activity或fragment的onActivityResult中加入这句

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        provider.onActivityResult(requestCode, resultCode, data);
    }

然后调用这几个方法就行了，很简单。  
`void getImageFromCamera(OnImageSelectListener mListener)`   
`void getImageFromAlbum(OnImageSelectListener mListener)`  
`void getImageFromAlbum(OnImageSelectListener mListener,int maxCount)`  
`void getImageFromCameraOrAlbum(OnImageSelectListener mListener)`  
`void getImageFromCameraOrAlbum(OnImageSelectListener mListener,int maxCount)`   
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

onImageLoaded会回调一个文件Uri。  
注意有时你的相机与相册返回的图片过大。不能直接读取所返回的uri。可以使用
`ImageProvider.readImageWithSize(uri,width,height);`以缩放大小加载图片。


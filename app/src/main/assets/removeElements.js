function deleteElements() {
    //删除h5-download-bar clearfix整个class元素
    deleteClassElement("h5-download-bar clearfix");

    //删除关注按钮attention-animation-holder
    deleteClassElement("attention-animation-holder");

    //删除头像的href属性
    deleteClassAttribute("author-face");

    //删除用户名的href属性
    deleteClassAttribute("author-name");

    //删除文章推荐部分
    deleteClassElement("new-iteration-list unselectable");

    //删除评论部分
    deleteClassElement("app");

    //删除多余的部分
    deleteClassElement("new-border");
    deleteClassElement("info-bar");
    deleteClassElement("tag-container");
    deleteClassElement("article-action clearfix");
}

//删除整个指定的class部分
function deleteClassElement (className) {
    paras = document.getElementsByClassName(className);
    for (i = 0; i < paras.length; i++) {

        //删除元素 元素.parentNode.removeChild(元素);
        if (paras[i] != null) {
            paras[i].parentNode.removeChild(paras[i]);
        }
    }
}

//删除指定元素中的href部分
function deleteClassAttribute(attributeName) {
    document.getElementsByClassName(attributeName).removeAttribute("href");
}
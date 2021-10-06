let tagsApp = new Vue({
    el:'#tagsApp',
    data:{
        tags:[]
    },
    methods:{
        loadTags:function () {
            console.log('执行了 loadTags');
            $.ajax({
                url:'/v1/tags',
                method:'GET',
                success:function (r) {
                    console.log(r);
                    if (r.code === OK){
                        console.log('成功获取tags');
                        //从空数组变为了拥有所有标签的数组
                        tagsApp.tags = r.data;
                    }
                }
            });
        }
    },

    //这个方法会在页面加载完毕之后立即执行
    created:function () {
        //调用上面编写的显示所有标签的方法
        this.loadTags();
    }
});
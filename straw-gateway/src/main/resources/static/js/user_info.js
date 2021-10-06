let userApp = new Vue({
    el:"#userApp",
    data:{
        user:{},
    },
    methods:{
        //加载当前用户
        loadCurrentUser:function (){
            console.log("方法创建");
            $.ajax({
                url:"/sys/v1/users/my",
                method:"get",
                success:function (r){
                    if(r.code==OK){
                        userApp.user=r.data;
                    }else {
                        alert(r.message)
                    }
                }
            })
        }
    },
    created:function (){
        this.loadCurrentUser();
    }
})
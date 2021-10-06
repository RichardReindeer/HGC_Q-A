let app = new Vue({
    el:'#app',
    data:{
        inviteCode:'',
        phone:'',
        nickname:'',
        password:'',
        confirm:'',
        message: '',
        hasError: false
    },
    methods:{
        //当用户点击注册的时候，执行这个方法
        register:function () {
            //将submit输出到控制台,打桩
            console.log('Submit');
            //为了将表单中的信息发送给java的Controller
            //我们需要定义这个局部变量data
            //真正和实体类属性必须一致的是这个data中的属性名
            let data = {
                //将用户输入的变量赋值给data中的对应属性
                inviteCode: this.inviteCode,
                phone: this.phone,
                nickname: this.nickname,
                password: this.password,
                confirm: this.confirm
            }

            if(data.password !== data.confirm){
                this.message = "两次输入密码不一致！";
                this.hasError = true;
                return;
            }
            console.log(data);//打桩
            //这里的this指的是vue对象app，
            //因为进入ajax方法后再使用this就是ajax对象了
            //想在ajax方法中使用vue绑定的变量，就必须使用vue变量的this
            let _this = this;
            $.ajax({
                //页面不跳转，但是请求发出去了
                url:"/register",
                method: "POST",
                data: data,
                //function(r) 中的r 就是java控制器返回的r类对象的json格式
                //例如:{"code":200,"message:":"ok","data":null}
                success: function (r) {
                    console.log(r);
                    if(r.code == CREATED){
                        console.log("注册成功");
                        console.log(r.message);
                        _this.hasError = false;
                        location.href = '/login.html?register';
                    }else{
                        console.log(r.message);
                        _this.hasError = true;
                        _this.message = r.message;
                    }
                }
            });
        }
    }
});
let searchApp=new Vue({
    el:"#searchApp",
    data:{
        key :""
    },
    methods:{
        search:function (){
            //encodeURI(key) 方式路径是中文时发生乱码
            //encodeURI是js的方法
            location.href="/search.html?key="+encodeURI(this.key)
        }
    }

})
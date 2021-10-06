Vue.component('v-select', VueSelect.VueSelect);
let createQuestionApp = new Vue({
    el:'#createQuestionApp',
    data:{
        title:'',
        selectedTags:[],
        tags:[],
        selectedTeachers:[],
        teachers:[]
    },
    methods:{
        createQuestion:function(){
            let content = $('#summernote').val();
            console.log(content);
            //data 对象，与服务器端QuestionVo的属性对应
            let data = {
                title:createQuestionApp.title,
                tagNames: this.selectedTags,
                teacherNicknames: this.selectedTeachers,
                content:content
            };
            console.log(data);
            $.ajax({
                url:'/faq/v1/questions',
                traditional: true,  //采用传统数组编码方式，SpringMVC才能接收
                method:'POST',
                data:data,
                success:function (r) {
                    console.log(r);
                    if(r.code === OK){
                        console.log(r.message);
                    }else{
                        console.log(r.message);
                    }
                }
            });
        },
        loadTags:function () {
            console.log("loadTags");
            $.ajax({
                url:'/faq/v1/tags',
                method: 'GET',
                success:function (r) {
                    console.log(r);
                    if(r.code == OK){
                        let list=r.data;
                        let tags = [];
                        for (let i=0;i<list.length; i++) {
                           tags.push(list[i].name);
                        }
                        createQuestionApp.tags = tags;
                    }
                }
            });
        },
        loadTeachers:function () {
            console.log("loadTeachers");
            $.ajax({
                url:'/sys/v1/users/masters',
                method: 'GET',
                success:function (r) {
                    console.log(r);
                    if(r.code == OK){
                        //因为r.data是一个tag类型的数组
                        //但是v-select 需要绑定的是一个内容是字符串的数组
                        let list=r.data;
                        //准备好字符串数组
                        let teachers = [];
                        //循环遍历所有标签
                        for (let i=0;i<list.length; i++) {
                            //只将标签名称赋值给tags数组
                            //js中数组对象调用push方法，相当于java中list对象的add
                            teachers.push(list[i].nickname);
                        }
                        //将包含所有标签名称的vue数组赋值为vue绑定的tags
                        createQuestionApp. teachers= teachers;
                    }
                }
            });
        }
    },
    created:function () {
        this.loadTags();
        this.loadTeachers();
    }
});
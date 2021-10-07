

/*
显示当前用户的热点问题
 */
let hotQuestion  = new Vue({
    el:'#hotQuestion',
    data: {
        questionsList:[],
        pageInfo:{},
    },
    methods: {
        loadQuestions:function () {
            $.ajax({
                url: '/faq/v1/questions/hotQuestion',
                method: "GET",
                success: function (r) {
                    console.log("成功显示热点数据数据");
                    console.log(r);
                    if(r.code === OK){
                        hotQuestion.questionsList = r.data.list;
                        hotQuestion.pageInfo = r.data;
                        //为question对象添加持续时间属性
                        hotQuestion.updateDuration();
                        hotQuestion.updateTagImage();
                    }
                }
            });
        },
        updateTagImage:function(){
            let questionsList = this.questionsList;
            for(let i=0; i<questionsList.length; i++){
                let tags = questionsList[i].tags;
                //js中if([变量]) 这种写法
                // 就是在判断这个变量存在与否，如果存在则返回true
                if(tags){

                    let tagImage = '/img/tags/'+tags[0].id+'.jpg';
                    console.log(tagImage);
                    questionsList[i].tagImage = tagImage;
                }
            }
        },
        updateDuration:function () {
            let questionsList = this.questionsList;
            // 遍历所有问题
            for(let i=0; i<questionsList.length; i++){
                addDuration(questionsList[i]);
            }
        },
    },
    created:function () {
        console.log("执行了显示热点问题的方法");
        this.loadQuestions()//默认查第一页
    }
});









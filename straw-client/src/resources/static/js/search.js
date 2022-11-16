
/*
显示当前用户的问题
 */
let questionsApp = new Vue({
    el:'#questionsApp',
    data: {
        questions:[],
        pageinfo:{},
    },
    methods: {
        loadQuestions:function (pageNum) {
            if(! pageNum){
                pageNum = 1;
            }
            let key = location.search;
            if(!key){
                return;
            }
            key.substring(1);

            decodeURI(key.substring(1));

            let formdata=new FormData;
            formdata.append("key",key);
            formdata.append("pageNum",pageNum);
            formdata.append("accessToken",token);
            axios({
                url: 'http://localhost:9000/v3/questions',
                method: "POST",
                data: formdata
            }).then(function(r){
                console.log("成功加载数据");
                console.log(r);
                if(r.status == OK){
                    questionsApp.questions = r.data.list;
                    questionsApp.pageinfo = r.data;
                    //为question对象添加持续时间属性
                    questionsApp.updateDuration();
                    questionsApp.updateTagImage();
                }
            })
        },
        updateTagImage:function(){
            let questions = this.questions;
            for(let i=0; i<questions.length; i++){
                let tags = questions[i].tags;
                if(tags){
                    let tagImage = '/img/tags/'+tags[0].id+'.jpg';
                    console.log(tagImage);
                    questions[i].tagImage = tagImage;
                }
            }
        },
        updateDuration:function () {
            let questions = this.questions;
            for(let i=0; i<questions.length; i++){
                addDuration(questions[i])}
        }
    },
    created:function () {
        console.log("执行了方法");
        this.loadQuestions(1);

    }
});

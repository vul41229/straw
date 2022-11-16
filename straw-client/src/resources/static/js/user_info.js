let userApp=new Vue({
    el:"#userApp",
    data:{
        user:{}
    },
    methods:{
        loadUserVO:function(){
            axios({
                url:"http://localhost:9000/v1/users/me",
                method:"get",
                params:{
                    accessToken:token
                }
            }).then(function(response){
                // 将控制器返回的userVO对象,绑定给当前vue的user
                userApp.user=response.data;
            })
        }
    },
    created:function(){
        // 页面加载完毕时运行的方法
        // 执行加载用户信息面板的方法
        this.loadUserVO();
    }
})
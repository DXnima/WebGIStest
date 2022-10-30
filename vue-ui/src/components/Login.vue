<template>
  <div class="login_container">
    <div class="login_box">
      <!--头像区域-->
      <div class="avatar_box">
        <img src="../assets/logo.svg"/>
      </div>
      <!--登陆区域-->
      <el-form ref="loginFormRef" :model="loginForm" :rules="loginFormRules" label-width="0px" class="login_form">
        <!--用户名-->
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <!--密码-->
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" prefix-icon="el-icon-s-cooperation" type="password"></el-input>
        </el-form-item>
        <!--按钮区域-->
        <el-form-item class="btns">
          <el-button type="primary" @click="login">登陆</el-button>
          <el-button type="info" @click="resetLoginForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        //这是登陆表单数据绑定
        loginForm:{
          username:'WebGISTest',
          password:'123456'
        },
        //这是表单验证规则
        loginFormRules:{
          //验证用户名规则
          username:[
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 3, max: 15, message: '长度在 3 到 15 之间', trigger: 'blur' }
            ],
          //验证密码规则
          password:[
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 6, max: 15, message: '密码长度在 3 到 15 之间', trigger: 'blur' }
          ]
        }
      };
    },
    methods :{
      //点击重置按钮 重置表单
      resetLoginForm() {
        this.$refs.loginFormRef.resetFields();
      },
      login() {
        this.$refs.loginFormRef.validate(async valid=>{
          if (!valid) return;
          // const {data:res}=await this.$http.post("login", this.loginForm );
          // if (res.meta.status!=200) return this.$message.error(res.meta.msg);
          this.$message.success('登陆成功');
          //保存token
          // window.sessionStorage.setItem('token',res.data.token);
          window.sessionStorage.setItem('token', 'token');
          //页面跳转
          this.$router.push("/home");
        });
      }
    }
  }
</script>

<style lang="less" scoped>
  .login_container{
    background-image: url("../assets/image/bg.jpg");
    height: 100%;
    background-size: 100%;
  }

  .login_box{
    width: 450px;
    height: 300px;
    background-color: #fff;
    border-radius: 3px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);

    .avatar_box{
      width: 130px;
      height: 130px;
      border: 1px solid #eee;
      border-radius: 50%;
      padding: 10px;
      box-shadow: 0 0 10px #ddd;
      position: absolute;
      left: 50%;
      transform: translate(-50%,-50%);
      background-color: #fff;
      img{
        width: 100%;
        height: 100%;
        border-radius: 50%;
        background-color: #eee;
      }
    }
  }

  .btns{
    display: flex;
    justify-content: flex-end;
  }

  .login_form{
    position: absolute;
    bottom: 0;
    width: 100%;
    padding: 0 20px;
    box-sizing: border-box;
  }
</style>

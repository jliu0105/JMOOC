<template>
  <div class="main-container">
    <div class="main-content">
      <div class="row">
        <div class="col-sm-10 col-sm-offset-1">
          <div class="login-container">
            <div class="center">
              <h1>
                <i class="ace-icon fa fa-leaf green"></i>
                <span class="">control panel login </span>
              </h1>
            </div>

            <div class="space-6"></div>

            <div class="position-relative">
              <div id="login-box" class="login-box visible widget-box no-border">
                <div class="widget-body">
                  <div class="widget-main">
                    <h4 class="header blue lighter bigger">
                      <i class="ace-icon fa fa-coffee green"></i>
                      please input username and password
                    </h4>

                    <div class="space-6"></div>

                    <form>
                      <fieldset>
                        <label class="block clearfix">
                          <span class="block input-icon input-icon-right">
                            <input v-model="user.loginName" type="text" class="form-control" placeholder="username"/>
                            <i class="ace-icon fa fa-user"></i>
                          </span>
                        </label>

                        <label class="block clearfix">
                          <span class="block input-icon input-icon-right">
                            <input v-model="user.password" type="password" class="form-control" placeholder="password"/>
                            <i class="ace-icon fa fa-lock"></i>
                          </span>
                        </label>

                        <label class="block clearfix">
                          <span class="block input-icon input-icon-right">
                            <div class="input-group">
                              <input v-model="user.imageCode" type="text" class="form-control" placeholder="verification code">
                              <span class="input-group-addon" id="basic-addon2">
                                <img v-on:click="loadImageCode()" id="image-code" alt="verification_code"/>
                              </span>
                            </div>
                          </span>
                        </label>

                        <div class="space"></div>

                        <div class="clearfix">
                          <label class="inline">
                            <input v-model="remember" type="checkbox" class="ace"/>
                            <span class="lbl">remember me</span>
                          </label>

                          <button type="button"
                                  class="width-35 pull-right btn btn-sm btn-primary"
                                  v-on:click="login()">
                            <i class="ace-icon fa fa-key"></i>
                            <span class="bigger-110">login</span>
                          </button>
                        </div>

                        <div class="space-4"></div>
                      </fieldset>
                    </form>

                  </div><!-- /.widget-main -->

                </div><!-- /.widget-body -->
              </div><!-- /.login-box -->
            </div><!-- /.position-relative -->

          </div>
        </div><!-- /.col -->
      </div><!-- /.row -->
    </div><!-- /.main-content -->
  </div><!-- /.main-container -->
</template>

<script>
  export default {
    name: "login",
    data: function() {
      return {
        user: {},
        remember: true, 
        imageCodeToken: ""
      }
    },
    mounted: function() {
      let _this = this;
      $("body").removeClass("no-skin");
      $("body").attr("class", "login-layout light-login");

      let rememberUser = LocalStorage.get(LOCAL_KEY_REMEMBER_USER);
      if (rememberUser) {
        _this.user = rememberUser;
      }

      _this.loadImageCode();
    },
    methods: {
      login () {
        let _this = this;

        let md5 = hex_md5(_this.user.password);
        let rememberUser = LocalStorage.get(LOCAL_KEY_REMEMBER_USER) || {};
        if (md5 !== rememberUser.md5) {
          _this.user.password = hex_md5(_this.user.password + KEY);
        }

        _this.user.imageCodeToken = _this.imageCodeToken;

        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/login', _this.user).then((response)=>{
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            console.log("login success：", resp.content);
            let loginUser = resp.content;
            Tool.setLoginUser(resp.content);

            if (_this.remember) {
              let md5 = hex_md5(_this.user.password);
              LocalStorage.set(LOCAL_KEY_REMEMBER_USER, {
                loginName: loginUser.loginName,
                password: _this.user.password,
                md5: md5
              });
            } else {
              LocalStorage.set(LOCAL_KEY_REMEMBER_USER, null);
            }
            _this.$router.push("/welcome");
          } else {
            Toast.warning(resp.message);
            _this.user.password = "";
            _this.loadImageCode();
          }
        });
      },

      loadImageCode: function () {
        let _this = this;
        _this.imageCodeToken = Tool.uuid(8);
        $('#image-code').attr('src', process.env.VUE_APP_SERVER + '/system/admin/kaptcha/image-code/' + _this.imageCodeToken);
      },
    }
  }
</script>

<style scoped>
  .input-group-addon {
    padding: 0;
  }
</style>

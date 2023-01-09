<template>
  <div id="login-modal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-login" role="document">
      <div class="modal-content">
        <div class="modal-body">
          <div class="login-div" v-show="MODAL_STATUS === STATUS_LOGIN">
            <h3>log&nbsp;&nbsp;in</h3>
            <div class="form-group">
              <input v-model="member.mobile" class="form-control" placeholder="phone_number">
            </div>
            <div class="form-group">
              <input class="form-control" type="password" placeholder="password" v-model="member.password">
            </div>
            <div class="form-group">
              <div class="input-group">
                <input id="image-code-input" class="form-control" type="text" placeholder="verification_code"
                       v-model="member.imageCode">
                <div class="input-group-addon" id="image-code-addon">
                  <img id="image-code" alt="verification_code" v-on:click="loadImageCode()"/>
                </div>
              </div>
            </div>
            <div class="form-group">
              <button v-on:click="login()" class="btn btn-primary btn-block submit-button">
                log&nbsp;&nbsp;in
              </button>
            </div>
            <div class="form-group">
              <div class="checkbox">
                <label>
                  <input type="checkbox" class="remember" v-model="remember"> remember password
                </label>
                <div class="pull-right">
                  <a href="javascript:;" v-on:click="toForgetDiv()">password_forget</a>&nbsp;
                  <a href="javascript:;" v-on:click="toRegisterDiv()">I need to register</a>
                </div>
              </div>
            </div>
            <div class="form-group to-register-div">
            </div>
          </div>
          <div class="register-div" v-show="MODAL_STATUS === STATUS_REGISTER">
            <h3>register</h3>
            <div class="form-group">
              <input v-on:blur="onRegisterMobileBlur()"
                     v-bind:class="registerMobileValidateClass"
                     id="register-mobile" v-model="memberRegister.mobile"
                     class="form-control" placeholder="phone number">
              <span v-show="registerMobileValidate === false" class="text-danger">10-digit mobile phone number, and cannot be repeated</span>
            </div>
            <div class="form-group">
              <div class="input-group">
                <input v-on:blur="onRegisterMobileCodeBlur()"
                       v-bind:class="registerMobileCodeValidateClass"
                       id="register-mobile-code" class="form-control"
                       placeholder="smsCode" v-model="memberRegister.smsCode">
                <div class="input-group-append">
                  <button class="btn btn-outline-secondary" id="register-send-code-btn"
                          v-on:click="sendSmsForRegister()">send verification code
                  </button>
                </div>
              </div>
              <span v-show="registerMobileCodeValidate === false" class="text-danger">Please enter the SMS 6-digit verification code</span>
            </div>
            <div class="form-group">
              <input v-on:blur="onRegisterNameBlur()"
                     v-bind:class="registerNameValidateClass"
                     id="register-name" v-model="memberRegister.name"
                     class="form-control" placeholder="nickname">
              <span v-show="registerNameValidate === false" class="text-danger">Nickname 2 to 20 characters, letters, numbers, underscore combination</span>
            </div>
            <div class="form-group">
              <input v-on:blur="onRegisterPasswordBlur()"
                     v-bind:class="registerPasswordValidateClass"
                     id="register-password" v-model="memberRegister.passwordOriginal"
                     class="form-control" placeholder="password" type="password">
              <span v-show="registerPasswordValidate === false" class="text-danger">The password must be at least 6 characters, including at least 1 letter and 1 number</span>
            </div>
            <div class="form-group">
              <input v-on:blur="onRegisterConfirmPasswordBlur()"
                     v-bind:class="registerConfirmPasswordValidateClass"
                     id="register-confirm-password" v-model="memberRegister.confirm"
                     class="form-control" placeholder="confirm password"
                     name="memberRegisterConfirm" type="password">
              <span v-show="registerConfirmPasswordValidate === false" class="text-danger">Confirm password and password match</span>
            </div>
            <div class="form-group">
              <button class="btn btn-primary btn-block submit-button" v-on:click="register()">
               register 
              </button>
            </div>
            <div class="form-group to-login-div">
              <a href="javascript:;" v-on:click="toLoginDiv()">I need to log in</a>
            </div>
          </div>
          <div class="forget-div" v-show="MODAL_STATUS === STATUS_FORGET">
            <h3>password_forget</h3>
            <div class="form-group">
              <input v-on:blur="onForgetMobileBlur()"
                     v-bind:class="forgetMobileValidateClass"
                     id="forget-mobile" v-model="memberForget.mobile"
                     class="form-control" placeholder="phone number">
              <span v-show="forgetMobileValidate === false" class="text-danger">The mobile phone number is 11 digits and must be registered</span>
            </div>
            <div class="form-group">
              <div class="input-group">
                <input v-on:blur="onForgetMobileCodeBlur()"
                       v-bind:class="forgetMobileCodeValidateClass"
                       id="forget-mobile-code" class="form-control"
                       placeholder="SMS code" v-model="memberForget.smsCode">
                <div class="input-group-append">
                  <button v-on:click="sendSmsForForget()"
                          class="btn btn-outline-secondary" id="forget-send-code-btn">
                    send verification code
                  </button>
                </div>
              </div>
              <span v-show="forgetMobileCodeValidate === false" class="text-danger">Please enter the SMS 6-digit verification code</span>
            </div>
            <div class="form-group">
              <input v-on:blur="onForgetPasswordBlur()"
                     v-bind:class="forgetPasswordValidateClass"
                     id="forget-password" v-model="memberForget.passwordOriginal"
                     class="form-control" placeholder="password" type="password">
              <span v-show="forgetPasswordValidate === false" class="text-danger">The password must be at least 6 characters, including at least 1 letter and 1 number</span>
            </div>
            <div class="form-group">
              <input v-on:blur="onForgetConfirmPasswordBlur()"
                     v-bind:class="forgetConfirmPasswordValidateClass"
                     id="forget-confirm-password" v-model="memberForget.confirm"
                     class="form-control" placeholder="confirm your password" type="password">
              <span v-show="forgetConfirmPasswordValidate === false" class="text-danger">Confirm password and password match</span>
            </div>
            <div class="form-group">
              <button v-on:click="resetPassword()" class="btn btn-primary btn-block submit-button">
                reset
              </button>
            </div>
            <div class="form-group to-login-div">
              <a href="javascript:;" v-on:click="toLoginDiv()">I want to log in</a>
            </div>
          </div>
        </div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
  </div><!-- /.modal -->
</template>

<script>

  export default {
    name: 'the-login',
    data: function () {
      return {
        STATUS_LOGIN: "STATUS_LOGIN",
        STATUS_REGISTER: "STATUS_REGISTER",
        STATUS_FORGET: "STATUS_FORGET",
        MODAL_STATUS: "",

        member: {},
        memberForget: {},
        memberRegister: {},

        remember: true, // remember password
        imageCodeToken: "",

        registerMobileValidate: null,
        registerMobileCodeValidate: null,
        registerPasswordValidate: null,
        registerNameValidate: null,
        registerConfirmPasswordValidate: null,

        forgetMobileValidate: null,
        forgetMobileCodeValidate: null,
        forgetPasswordValidate: null,
        forgetConfirmPasswordValidate: null,
      }
    },
    computed: {
      registerMobileValidateClass: function () {
        return {
          'border-success': this.registerMobileValidate === true,
          'border-danger': this.registerMobileValidate === false,
        }
      },
      registerMobileCodeValidateClass: function () {
        return {
          'border-success': this.registerMobileCodeValidate === true,
          'border-danger': this.registerMobileCodeValidate === false,
        }
      },
      registerPasswordValidateClass: function () {
        return {
          'border-success': this.registerPasswordValidate === true,
          'border-danger': this.registerPasswordValidate === false,
        }
      },
      registerNameValidateClass: function () {
        return {
          'border-success': this.registerNameValidate === true,
          'border-danger': this.registerNameValidate === false,
        }
      },
      registerConfirmPasswordValidateClass: function () {
        return {
          'border-success': this.registerConfirmPasswordValidate === true,
          'border-danger': this.registerConfirmPasswordValidate === false,
        }
      },
      forgetMobileValidateClass: function () {
        return {
          'border-success': this.forgetMobileValidate === true,
          'border-danger': this.forgetMobileValidate === false,
        }
      },
      forgetMobileCodeValidateClass: function () {
        return {
          'border-success': this.forgetMobileCodeValidate === true,
          'border-danger': this.forgetMobileCodeValidate === false,
        }
      },
      forgetPasswordValidateClass: function () {
        return {
          'border-success': this.forgetPasswordValidate === true,
          'border-danger': this.forgetPasswordValidate === false,
        }
      },
      forgetConfirmPasswordValidateClass: function () {
        return {
          'border-success': this.forgetConfirmPasswordValidate === true,
          'border-danger': this.forgetConfirmPasswordValidate === false,
        }
      },
    },
    mounted() {
      let _this = this;
      _this.toLoginDiv();

      _this.$event.$on("openLoginModal", function (param) {
        console.log(param);
        // debugger;
        _this.openLoginModal();
      })
    },
    methods: {

      openLoginModal() {
        let _this = this;
        $("#login-modal").modal("show");
      },

      toLoginDiv() {
        let _this = this;

        // Get the remembered username and password from the cache. If you can't get it, it means you didn't check "Remember me" last time
        let rememberMember = LocalStorage.get(LOCAL_KEY_REMEMBER_MEMBER);
        if (rememberMember) {
          _this.member = rememberMember;
        }

        // Refresh the verification code image when the login box is displayed
        _this.loadImageCode();

        _this.MODAL_STATUS = _this.STATUS_LOGIN
      },
      toRegisterDiv() {
        let _this = this;
        _this.MODAL_STATUS = _this.STATUS_REGISTER
      },
      toForgetDiv() {
        let _this = this;
        _this.MODAL_STATUS = _this.STATUS_FORGET
      },

      register() {
        let _this = this;

          _this.onRegisterMobileCodeBlur() &&
          _this.onRegisterNameBlur() &&
          _this.onRegisterPasswordBlur() &&
          _this.onRegisterConfirmPasswordBlur();
        if (!validateResult) {
          return;
        }

        _this.memberRegister.password = hex_md5(_this.memberRegister.passwordOriginal + KEY);

        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/web/member/register', _this.memberRegister).then((response) => {
          let resp = response.data;
          if (resp.success) {
            Toast.success("register success");
          } else {
            Toast.warning(resp.message);
          }
        })
      },


      //---------------login-----------------
      login () {
        let _this = this;

        // If the password is brought from the cache, no re-encryption is required
        let md5 = hex_md5(_this.member.password);
        let rememberMember = LocalStorage.get(LOCAL_KEY_REMEMBER_MEMBER) || {};
        if (md5 !== rememberMember.md5) {
          _this.member.password = hex_md5(_this.member.password + KEY);
        }

        _this.member.imageCodeToken = _this.imageCodeToken;

        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/web/member/login', _this.member).then((response)=>{
          let resp = response.data;
          if (resp.success) {
            console.log("log in success：", resp.content);
            let loginMember = resp.content;
            Tool.setLoginMember(resp.content);

            if (_this.remember) {
              let md5 = hex_md5(_this.member.password);
              LocalStorage.set(LOCAL_KEY_REMEMBER_MEMBER, {
                mobile: loginMember.mobile,
                password: _this.member.password,
                md5: md5
              });
            } else {
              LocalStorage.set(LOCAL_KEY_REMEMBER_MEMBER, null);
            }

            // log in success
            _this.$parent.setLoginMember(loginMember);
            $("#login-modal").modal("hide");


          } else {
            Toast.warning(resp.message);
            _this.member.password = "";
            _this.loadImageCode();
          }
        });
      },
      loadImageCode: function () {
        let _this = this;
        _this.imageCodeToken = Tool.uuid(8);
        $('#image-code').attr('src', process.env.VUE_APP_SERVER + '/business/web/kaptcha/image-code/' + _this.imageCodeToken);
      },

      sendSmsForRegister() {
        let _this = this;

        if (!_this.onRegisterMobileBlur()) {
          return false;
        }

        let sms = {
          mobile: _this.memberRegister.mobile,
          use: SMS_USE.REGISTER.key
        };

        _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/web/member/is-mobile-exist/' + _this.memberRegister.mobile).then((res)=>{
          let response = res.data;
          if (response.success) {
            Toast.warning("phone number has been registered");
          } else {
            _this.sendSmsCode(sms, "register-send-code-btn");
          }
        })
      },

      sendSmsCode(sms, btnId) {
        let _this = this;

        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/web/sms/send', sms).then((res)=> {
          let response = res.data;
          if (response.success) {
            Toast.success("The SMS has been sent, please log in to the console admin.jiawahome.com to check the SMS verification code, username and password:test/test");

            _this.countdown = 60;
            _this.setTime(btnId);
          } else {
            Toast.warning(response.message);
          }
        })
      },

      setTime(btnId) {
        let _this = this;
        let btn = $("#" + btnId);
        if (_this.countdown === 0) {
          btn.removeAttr("disabled");
          btn.text("get verification");
          return;
        } else {
          btn.attr("disabled", true);
          btn.text("resend(" + _this.countdown + ")");
          _this.countdown--;
        }
        setTimeout(function () {
          _this.setTime(btnId);
        }, 1000);
      },

      sendSmsForForget() {
        let _this = this;
        if (!_this.onForgetMobileBlur()) {
          return false;
        }
        let sms = {
          mobile: _this.memberForget.mobile,
          use: SMS_USE.FORGET.key
        };

        _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/web/member/is-mobile-exist/' + _this.memberForget.mobile).then((res)=>{
          let response = res.data;
          if (response.success) {
            _this.forgetMobileValidate = true;
            _this.sendSmsCode(sms, "forget-send-code-btn");
          } else {
            _this.forgetMobileValidate = false;
            Toast.warning("phone number has not register");
          }
        });
      },

      resetPassword() {
        let _this = this;

        let validateResult = _this.onForgetMobileBlur() &&
          _this.onForgetMobileCodeBlur() &&
          _this.onForgetPasswordBlur() &&
          _this.onForgetConfirmPasswordBlur();
        if (!validateResult) {
          return;
        }

        _this.memberForget.password = hex_md5(_this.memberForget.passwordOriginal + KEY);

        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/web/member/reset-password', _this.memberForget).then((res)=>{
          let response = res.data;
          if (response.success) {
            Toast.success("password reset success");
            _this.toLoginDiv();
          } else {
            Toast.warning(response.message);
          }
        }).catch((response)=>{
          console.log("error：", response);
        })
      },

      //-------------------------------- register ----------------------------

      onRegisterMobileBlur () {
        let _this = this;
        _this.registerMobileValidate = Pattern.validateMobile(_this.memberRegister.mobile);
        return _this.registerMobileValidate;
      },

      onRegisterMobileCodeBlur () {
        let _this = this;
        _this.registerMobileCodeValidate = Pattern.validateMobileCode(_this.memberRegister.smsCode);
        return _this.registerMobileValidate;
      },

      onRegisterNameBlur () {
        let _this = this;
        _this.registerNameValidate = Pattern.validateName(_this.memberRegister.name);
        return _this.registerMobileValidate;
      },

      onRegisterPasswordBlur () {
        let _this = this;
        _this.registerPasswordValidate = Pattern.validatePasswordWeak(_this.memberRegister.passwordOriginal);
        return _this.registerMobileValidate;
      },

      onRegisterConfirmPasswordBlur () {
        let _this = this;
        let confirmPassword = $("#register-confirm-password").val();
        if (Tool.isEmpty(confirmPassword)) {
          return _this.registerConfirmPasswordValidate = false;
        }
        return _this.registerConfirmPasswordValidate = (confirmPassword === _this.memberRegister.passwordOriginal);
      },

      //-------------------------------- password_forget ----------------------------

      onForgetMobileBlur () {
        let _this = this;
        return _this.forgetMobileValidate = Pattern.validateMobile(_this.memberForget.mobile);
      },

      onForgetMobileCodeBlur () {
        let _this = this;
        return _this.forgetMobileCodeValidate = Pattern.validateMobileCode(_this.memberForget.smsCode);
      },

      onForgetPasswordBlur () {
        let _this = this;
        return _this.forgetPasswordValidate = Pattern.validatePasswordWeak(_this.memberForget.passwordOriginal);
      },

      onForgetConfirmPasswordBlur () {
        let _this = this;
        let forgetPassword = $("#forget-confirm-password").val();
        if (Tool.isEmpty(forgetPassword)) {
          return _this.forgetConfirmPasswordValidate = false;
        }
        return _this.forgetConfirmPasswordValidate = (forgetPassword === _this.memberForget.passwordOriginal);
      }
    }
  }
</script>

<style scoped>
  /* log in */
  .login-div .input-group-addon {
    padding: 0;
    border: 0;
  }

  #login-modal h3 {
    text-align: center;
    margin-bottom: 20px;
  }

  #login-modal .modal-login {
    max-width: 400px;
  }

  #login-modal input:not(.remember) {
    height: 45px;
    font-size: 16px;
  }

  #login-modal .submit-button {
    height: 50px;
    font-size: 20px;
  }

  #login-modal .to-login-div {
    text-align: center;
  }
</style>

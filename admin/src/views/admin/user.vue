<template>
  <div>
    <p>
      <button v-show="hasResource('010101')" v-on:click="add()" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-edit"></i>
        add
      </button>
      &nbsp;
      <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh"></i>
        refresh
      </button>
    </p>

    <pagination ref="pagination" v-bind:list="list" v-bind:itemCount="8"></pagination>

    <table id="simple-table" class="table  table-bordered table-hover">
      <thead>
      <tr>
        <th>id</th>
        <th>username</th>
        <th>nickname</th>
        <th>password</th>
        <th>action</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="user in users">
        <td>{{user.id}}</td>
        <td>{{user.loginName}}</td>
        <td>{{user.name}}</td>
        <td>{{user.password}}</td>
      <td>
        <div class="hidden-sm hidden-xs btn-group">
          <button v-show="hasResource('010103')" v-on:click="editPassword(user)" class="btn btn-xs btn-info">
            <i class="ace-icon fa fa-key bigger-120"></i>
          </button>
          <button v-show="hasResource('010101')" v-on:click="edit(user)" class="btn btn-xs btn-info">
            <i class="ace-icon fa fa-pencil bigger-120"></i>
          </button>
          <button v-show="hasResource('010102')" v-on:click="del(user.id)" class="btn btn-xs btn-danger">
            <i class="ace-icon fa fa-trash-o bigger-120"></i>
          </button>
        </div>
      </td>
      </tr>
      </tbody>
    </table>

    <div id="form-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">form</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label class="col-sm-2 control-label">username</label>
                <div class="col-sm-10">
                  <input v-model="user.loginName" v-bind:disabled="user.id" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">nickname</label>
                <div class="col-sm-10">
                  <input v-model="user.name" class="form-control">
                </div>
              </div>
              <div v-show="!user.id" class="form-group">
                <label class="col-sm-2 control-label">password</label>
                <div class="col-sm-10">
                  <input v-model="user.password" type="password" class="form-control">
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">cancel</button>
            <button v-on:click="save()" type="button" class="btn btn-primary">save</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <div id="edit-password-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">change password</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label class="control-label col-sm-2">password</label>
                <div class="col-sm-10">
                  <input class="form-control" type="password" v-model="user.password" name="password">
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-white btn-default btn-round" data-dismiss="modal">
              <i class="ace-icon fa fa-times"></i>
              cancel
            </button>
            <button type="button" class="btn btn-white btn-info btn-round" v-on:click="savePassword()">
              <i class="ace-icon fa fa-plus blue"></i>
              save password
            </button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
  </div>
</template>

<script>
  import Pagination from "../../components/pagination";
  export default {
    components: {Pagination},
    name: "system-user",
    data: function() {
      return {
        user: {},
        users: [],
      }
    },
    mounted: function() {
      let _this = this;
      _this.$refs.pagination.size = 5;
      _this.list(1);
    },
    methods: {
      hasResource(id) {
        return Tool.hasResource(id);
      },

      add() {
        let _this = this;
        _this.user = {};
        $("#form-modal").modal("show");
      },

      edit(user) {
        let _this = this;
        _this.user = $.extend({}, user);
        $("#form-modal").modal("show");
      },

      list(page) {
        let _this = this;
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/list', {
          page: page,
          size: _this.$refs.pagination.size,
        }).then((response)=>{
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            _this.users = resp.content.list;
            _this.$refs.pagination.render(page, resp.content.total);
          }
        })
      },

      save() {
        let _this = this;

        if (1 != 1
          || !Validator.require(_this.user.loginName, "login_name")
          || !Validator.length(_this.user.loginName, "login_name", 1, 50)
          || !Validator.length(_this.user.name, "nickname", 1, 50)
          || !Validator.require(_this.user.password, "password")
        ) {
          return;
        }

        _this.user.password = hex_md5(_this.user.password + KEY);
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/save', _this.user).then((response)=>{
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            $("#form-modal").modal("hide");
            _this.list(1);
            Toast.success("save success！");
          } else {
            Toast.warning(resp.message)
          }
        })
      },

      del(id) {
        let _this = this;
        Confirm.show("are you sure you want to delete？", function () {
          Loading.show();
          _this.$ajax.delete(process.env.VUE_APP_SERVER + '/system/admin/user/delete/' + id).then((response)=>{
            Loading.hide();
            let resp = response.data;
            if (resp.success) {
              _this.list(1);
              Toast.success("delete succss！");
            }
          })
        });
      },

      editPassword(user) {
        let _this = this;
        _this.user = $.extend({}, user);
        _this.user.password = null;
        $("#edit-password-modal").modal("show");
      },

      savePassword() {
        let _this = this;

        _this.user.password = hex_md5(_this.user.password + KEY);
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/save-password', _this.user).then((response)=>{
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            $("#edit-password-modal").modal("hide");
            _this.list(1);
            Toast.success("save success！");
          } else {
            Toast.warning(resp.message)
          }
        })
      },
    }
  }
</script>

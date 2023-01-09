<template>
  <div>
    <p>
      <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh"></i>
        refresh
      </button>
    </p>

    <div class="row">
      <div class="col-md-6">
        <textarea id="resource-textarea" class="form-control" v-model="resourceStr" name="resource" rows="10"></textarea>

        <br>
        <button id="save-btn" type="button" class="btn btn-primary" v-on:click="save()">
          save
        </button>
      </div>
      <div class="col-md-6">
        <ul id="tree" class="ztree"></ul>
      </div>
    </div>

  </div>
</template>

<script>
  export default {
    name: "system-resource",
    data: function() {
      return {
        resource: {},
        resources: [],
        resourceStr: "",
        tree: {},
      }
    },
    mounted: function() {
      let _this = this;
      _this.list();
    },
    methods: {
      list() {
        let _this = this;
        Loading.show();
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/system/admin/resource/load-tree').then((res)=>{
          Loading.hide();
          let response = res.data;
          _this.resources = response.content;
          _this.initTree();
        })
      },

      save() {
        let _this = this;

        if (Tool.isEmpty(_this.resourceStr)) {
          Toast.warning("source cannot be empty！");
          return;
        }
        let json = JSON.parse(_this.resourceStr);

        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/resource/save', json).then((response)=>{
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
          _this.$ajax.delete(process.env.VUE_APP_SERVER + '/system/admin/resource/delete/' + id).then((response)=>{
            Loading.hide();
            let resp = response.data;
            if (resp.success) {
              _this.list(1);
              Toast.success("delete success！");
            }
          })
        });
      },

      initTree() {
        let _this = this;
        let setting = {
          data: {
            simpleData: {
              idKey: "id",
              pIdKey: "parent",
              rootPId: "",
              // enable: true
            }
          }
        };

        _this.zTree = $.fn.zTree.init($("#tree"), setting, _this.resources);
        _this.zTree.expandAll(true);
      },
    }
  }
</script>
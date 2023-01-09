<template>
  <div>
    <p>
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
        <th>phone number</th>
        <th>verification code</th>
        <th>usage</th>
        <th>start time</th>
        <th>usage</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="sms in smss">
        <td>{{sms.id}}</td>
        <td>{{sms.mobile}}</td>
        <td>{{sms.code}}</td>
        <td>{{SMS_USE | optionKV(sms.use)}}</td>
        <td>{{sms.at}}</td>
        <td>{{SMS_STATUS | optionKV(sms.status)}}</td>
      </tr>
      </tbody>
    </table>

  </div>
</template>

<script>
  import Pagination from "../../components/pagination";
  export default {
    components: {Pagination},
    name: "business-sms",
    data: function() {
      return {
        sms: {},
        smss: [],
        SMS_USE: SMS_USE,
        SMS_STATUS: SMS_STATUS,
      }
    },
    mounted: function() {
      let _this = this;
      _this.$refs.pagination.size = 5;
      _this.list(1);

    },
    methods: {
      add() {
        let _this = this;
        _this.sms = {};
        $("#form-modal").modal("show");
      },

      edit(sms) {
        let _this = this;
        _this.sms = $.extend({}, sms);
        $("#form-modal").modal("show");
      },

      list(page) {
        let _this = this;
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/sms/list', {
          page: page,
          size: _this.$refs.pagination.size,
        }).then((response)=>{
          Loading.hide();
          let resp = response.data;
          _this.smss = resp.content.list;
          _this.$refs.pagination.render(page, resp.content.total);

        })
      },
    }
  }
</script>

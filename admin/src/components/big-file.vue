<template>
  <div>
    <button type="button" v-on:click="selectFile()" class="btn btn-white btn-default btn-round">
      <i class="ace-icon fa fa-upload"></i>
      {{text}}
    </button>
    <input class="hidden" type="file" ref="file" v-on:change="uploadFile()" v-bind:id="inputId+'-input'">
  </div>
</template>

<script>
  export default {
    name: 'big-file',
    props: {
      text: {
        default: "upload large file"
      },
      inputId: {
        default: "file-upload"
      },
      suffixs: {
        default: []
      },
      use: {
        default: ""
      },
      shardSize: {
        default: 50 * 1024
      },
      url: {
        default: "oss-append"
      },
      afterUpload: {
        type: Function,
        default: null
      },
    },
    data: function () {
      return {
      }
    },
    methods: {
      uploadFile () {
        let _this = this;
        let formData = new window.FormData();
        let file = _this.$refs.file.files[0];

        console.log(JSON.stringify(file));
        /*
          name: "test.mp4"
          lastModified: 1901173357457
          lastModifiedDate: Tue May 27 2099 14:49:17 GMT+0800 () {}
          webkitRelativePath: ""
          size: 37415970
          type: "video/mp4"
        */

        let key = hex_md5(file.name + file.size + file.type);
        let key10 = parseInt(key, 16);
        let key62 = Tool._10to62(key10);
        console.log(key, key10, key62);
        console.log(hex_md5(Array()));
        /*
          d41d8cd98f00b204e9800998ecf8427e
          2.8194976848941264e+38
          6sfSqfOwzmik4A4icMYuUe
         */

        let suffixs = _this.suffixs;
        let fileName = file.name;
        let suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length).toLowerCase();
        let validateSuffix = false;
        for (let i = 0; i < suffixs.length; i++) {
          if (suffixs[i].toLowerCase() === suffix) {
            validateSuffix = true;
            break;
          }
        }
        if (!validateSuffix) {
          Toast.warning("The file format is incorrect! Only uploads are supported:" + suffixs.join(","));
          $("#" + _this.inputId + "-input").val("");
          return;
        }

        // let shardSize = 10 * 1024 * 1024;    
        // let shardSize = 50 * 1024;    
        let shardSize = _this.shardSize;
        let shardIndex = 1;		
        let size = file.size;
        let shardTotal = Math.ceil(size / shardSize); 

        let param = {
          'shardIndex': shardIndex,
          'shardSize': shardSize,
          'shardTotal': shardTotal,
          'use': _this.use,
          'name': file.name,
          'suffix': suffix,
          'size': file.size,
          'key': key62
        };

        _this.check(param);
      },



      check (param) {
        let _this = this;
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/file/admin/check/' + param.key).then((response)=>{
          let resp = response.data;
          if (resp.success) {
            let obj = resp.content;
            if (!obj) {
              param.shardIndex = 1;
              console.log("No file record found, upload from part 1");
              _this.upload(param);
            } else if (obj.shardIndex === obj.shardTotal) {
              Toast.success("upload file success！");
              _this.afterUpload(resp);
              $("#" + _this.inputId + "-input").val("");
            }  else {
              param.shardIndex = obj.shardIndex + 1;
              console.log("Find the file record, from the shard" + param.shardIndex + "start uploading");
              _this.upload(param);
            }
          } else {
            Toast.warning("file upload failed");
            $("#" + _this.inputId + "-input").val("");
          }
        })
      },

      upload (param) {
        let _this = this;
        let shardIndex = param.shardIndex;
        let shardTotal = param.shardTotal;
        let shardSize = param.shardSize;
        let fileShard = _this.getFileShard(shardIndex, shardSize);
        let fileReader = new FileReader();

        Progress.show(parseInt((shardIndex - 1) * 100 / shardTotal));
        fileReader.onload = function (e) {
          let base64 = e.target.result;
          // console.log("base64:", base64);

          param.shard = base64;

          _this.$ajax.post(process.env.VUE_APP_SERVER + '/file/admin/' + _this.url, param).then((response) => {
            let resp = response.data;
            console.log("upload file success：", resp);
            Progress.show(parseInt(shardIndex * 100 / shardTotal));
            if (shardIndex < shardTotal) {
              param.shardIndex = param.shardIndex + 1;
              _this.upload(param);
            } else {
              Progress.hide();
              _this.afterUpload(resp);
              $("#" + _this.inputId + "-input").val("");
            }
          });
        };
        fileReader.readAsDataURL(fileShard);
      },

      getFileShard (shardIndex, shardSize) {
        let _this = this;
        let file = _this.$refs.file.files[0];
        let start = (shardIndex - 1) * shardSize;	
        let end = Math.min(file.size, start + shardSize); 
        let fileShard = file.slice(start, end); 
        return fileShard;
      },

      selectFile () {
        let _this = this;
        $("#" + _this.inputId + "-input").trigger("click");
      }
    }
  }
</script>

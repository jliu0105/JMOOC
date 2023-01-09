<template>
  <div class="pagination" role="group" aria-label="pagination">
    <button type="button" class="btn btn-outline-dark" v-bind:disabled="page === 1" v-on:click="selectPage(1)">
      1
    </button>
    <button type="button" class="btn btn-outline-dark" v-bind:disabled="page === 1" v-on:click="selectPage(page - 1)">
      previous page
    </button>
    <button v-for="p in pages" v-bind:id="'page-' + p" type="button"
            v-bind:class="{'btn-primary active':page == p}" class="btn btn-outline-dark" v-on:click="selectPage(p)">
      {{p}}
    </button>
    <button type="button" class="btn btn-outline-dark" v-bind:disabled="page === pageTotal" v-on:click="selectPage(page + 1)">
      next page
    </button>
    <button type="button" class="btn btn-outline-dark" v-bind:disabled="page === pageTotal" v-on:click="selectPage(pageTotal)">
      {{pageTotal||1}}
    </button>
  </div>
</template>

<script>
  export default {
    name: 'pagination',
    props: {
      list: {
        type: Function,
        default: null
      },
      itemCount: Number // The number of pages displayed, for example, there are 100 pages in total, only 10 pages are displayed, and others are indicated by ellipsis
    },
    data: function () {
      return {
        total: 0, 
        size: 10, 
        page: 0, 
        pageTotal: 0, 
        pages: [], 
      }
    },
    methods: {
      /**
       * @param page
       * @param total
       */
      render(page, total) {
        let _this = this;
        _this.page = page;
        _this.total = total;
        _this.pageTotal = Math.ceil(total / _this.size);
        _this.pages = _this.getPageItems(_this.pageTotal, page, _this.itemCount || 10);
      },

      /**
       * @param page
       */
      selectPage(page) {
        let _this = this;
        if (page < 1) {
          page = 1;
        }
        if (page > _this.pageTotal) {
          page = _this.pageTotal;
        }
        if (this.page !== page) {
          _this.page = page;
          if (_this.list) {
            _this.list(page);
          }
        }
      },

      /**
       * @param total
       * @param current
       * @param length
       * @returns {Array}
       */
      getPageItems(total, current, length) {
        let items = [];
        if (length >= total) {
          for (let i = 1; i <= total; i++) {
            items.push(i);
          }
        } else {
          let base = 0;
          // move forward
          if (current - 0 > Math.floor((length - 1) / 2)) {
            // move backward
            base = Math.min(total, current - 0 + Math.ceil((length - 1) / 2)) - length;
          }
          for (let i = 1; i <= length; i++) {
            items.push(base + i);
          }
        }
        return items;
      }
    }
  }
</script>

<style scoped>
  .pagination {
    vertical-align: middle !important;
    font-size: 1rem;
    margin-top: 0;
    margin-bottom: 1rem;
  }
  .pagination button{
    margin-right: 0.5rem;
  }
  .btn-primary.active {
    background-color: #2f7bba!important;
    border-color: #27689d !important;
    color: white !important;
    font-weight: 600;
  }
</style>

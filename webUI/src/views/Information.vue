<template>
<el-dialog center title="블록체인 정보" :visible.sync="blockVisible" width="700px" append-to-body>
  <el-row :gutter="20">
    <el-col span="24">
    【Block number】：<el-tag>{{blockInfo.data.header.number}}</el-tag>
    </el-col>
    <br>
    <br>
    <el-col span="24">
    【Predecessor Hash】：{{blockInfo.data.header.previous_hash}}
    </el-col>
    <br>
    <br>
    <el-col span="24">
    【Data Hash】：{{blockInfo.data.header.data_hash}}
    </el-col>
    <br>
    <br>
    <el-col span="24">
    【Transaction ID】 ：{{blockInfo.data.data.data[0].payload.header.channel_header.tx_id}}
    </el-col>
    <br>
    <br>
    <el-col span="24">
    【Timestamp】：{{blockInfo.data.data.data[0].payload.header.channel_header.timestamp}}
    </el-col>
    <br>
    <br>
    <el-col span="24">
      【Channel】：<el-tag type="success">{{blockInfo.data.data.data[0].payload.header.channel_header.channel_id}}</el-tag>
    </el-col>
    <br>
    <br>
    <el-col span="24">
      【Blockchain Smart Contract】：<el-tag type="info">{{blockInfo.data.data.data[0].payload.data.actions[0].payload.action.proposal_response_payload.extension.chaincode_id.name}}</el-tag>
    </el-col>
    <br>
    <br>
    <el-col span="24">
      【Smart Contract Version】：<el-tag type="warning">{{blockInfo.data.data.data[0].payload.data.actions[0].payload.action.proposal_response_payload.extension.chaincode_id.version}}</el-tag>
    </el-col>

  </el-row>
</el-dialog>
</template>

<script>
export default {
  name: "BlockInfo",
  data(){
    return{
      blockInfo:{},
      blockVisible:false,
      dataDetail:null
    }
  },

  methods:{
    init(blockInfo){
      this.blockVisible = true
      this.blockInfo = blockInfo
      this.dataDetail = JSON.parse(blockInfo.data.data.data[0].payload.data.actions[0].payload.action.proposal_response_payload.extension.results.ns_rwset[0].rwset.writes[0].value)
    }
  }
}
</script>

<style scoped>

</style>

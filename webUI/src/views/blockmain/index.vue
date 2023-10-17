<template>
	<div class="app-container">
		<el-card style="width: 70%;height:100%;float: left;margin-left: 15px;">
			<div align="center">
				<el-tag>블</el-tag>&nbsp;&nbsp;&nbsp;
				<el-tag type="success">록</el-tag>&nbsp;&nbsp;&nbsp;
				<el-tag type="info">체</el-tag>&nbsp;&nbsp;&nbsp;
				<el-tag type="warning">인</el-tag>&nbsp;&nbsp;&nbsp;
				<el-tag type="danger">정</el-tag>&nbsp;&nbsp;&nbsp;
				<el-tag>보</el-tag>
			</div><br>
			<el-card>
				<el-row>
					<el-col :span="24">Block number：{{ blockInfo.data.header.number }}</el-col>


				</el-row>
				<br>
				<el-row>
					<el-col :span="24">Predecessor Hash：<el-link @click="queryInfo(blockInfo.data.header.previous_hash)" type="success">{{ blockInfo.data.header.previous_hash }}</el-link></el-col>
				</el-row>
				<br>
				<el-row>
					<el-col :span="24">Data Hash：<el-link type="success">{{ blockInfo.data.header.data_hash }}</el-link></el-col>
				</el-row>
			</el-card>
			<div align="center">
			<i class="el-icon-top"></i>
			<br>
			</div>
			<el-card>
				<el-row>
					<el-col :span="24">Block number：{{ previousBlockInfo.data.header.number }}</el-col>
				</el-row>
				<br>
				<el-row>
					<el-col :span="24">Predecessor Hash：<el-link @click="queryInfo(previousBlockInfo.data.header.previous_hash)" type="success">{{ previousBlockInfo.data.header.previous_hash }}</el-link></el-col>
				</el-row>
				<br>
				<el-row>
					<el-col :span="24">Data Hash：<el-link type="success">{{ previousBlockInfo.data.header.data_hash }}</el-link></el-col>
				</el-row>
			</el-card>
			<div align="center">
			<i class="el-icon-top"></i>
			<br>
			</div>

			<el-card>
				<el-row>
					<el-col :span="24">Block number：{{ previousBlockInfo2.data.header.number }}</el-col>
				</el-row>
				<br>
				<el-row>
					<el-col :span="24">Predecessor Hash：<el-link @click="queryInfo(previousBlockInfo2.data.header.previous_hash)" type="success">{{ previousBlockInfo2.data.header.previous_hash }}</el-link></el-col>
				</el-row>
				<br>
				<el-row>
					<el-col :span="24">Data Hash：<el-link type="success">{{ previousBlockInfo2.data.header.data_hash }}</el-link></el-col>
				</el-row>
			</el-card>


		</el-card>

		<el-card style="width: 27%;height: 100%;margin-left: 15px;float: right;">
			<div align="center">
				<font color="cornflowerblue">블록체인 네트워크 정보</font>
			</div><br>
			<el-card style="width: 100%;">
				<div align="center">
          Block Height : <font color="blueviolet" style="font-size: 30px;">{{channelBlockInfo.data.height.low}}</font>
				</div>
			</el-card>
			<el-card style="width: 100%;">
				<div align="center">
          Channel : <font color="blueviolet">tracechannel</font>
				</div>
			</el-card>
			<el-card style="width: 100%;">
				<div align="center">
					Peer<br><br>
       Certificate Authority:<el-tag type="danger">ca.trace.com</el-tag>&nbsp;&nbsp;&nbsp;<br><br>
       Consensus:<el-tag type="warning">orderer.trace.com</el-tag>&nbsp;&nbsp;&nbsp;<br><br>
        농가:<el-tag>peer0.org1.trace.com</el-tag>&nbsp;&nbsp;&nbsp;<br><br>
        원료공장:<el-tag>peer0.org2.trace.com</el-tag>&nbsp;&nbsp;&nbsp;<br><br>
				가공공장:<el-tag>peer0.org3.trace.com</el-tag>&nbsp;&nbsp;&nbsp;<br><br>
				판매자:<el-tag>peer0.org4.trace.com</el-tag>&nbsp;&nbsp;&nbsp;<br><br>
				물류:<el-tag>peer0.org5.trace.com</el-tag>&nbsp;&nbsp;&nbsp;<br><br>
				</div>
			</el-card>
		</el-card>

		<el-dialog center title="정보" :visible.sync="open" width="900px" append-to-body>
			<json-viewer :value="blockInfoHash" expand-depth=5 copyable boxed></json-viewer>
		</el-dialog>
	</div>
</template>

<script>
	import axios from 'axios'
	export default{
		data(){
			return{
				queryBlockByNumDrawer:false,
				channelBlockInfo:'',
				blockInfo:'',
				previousBlockInfo:'',
				previousBlockInfo2:'',
				open:false,
				blockInfoHash:'',
			}
		},
		created() {
			this.$httpBlock.get(this.$httpUrl+"/blockexplorerapi/channelBlockInfo")
			.then(res => {
				this.channelBlockInfo = res;
				var number = Number(this.channelBlockInfo.data.height.low - 1);
				this.$httpBlock.get(this.$httpUrl+"/blockexplorerapi/queryBlockInfo?number="+number)
				.then(res => {
					this.blockInfo = res;
				}).catch(err => {
					console.log("err "+err)
				})

				var previous = Number(this.channelBlockInfo.data.height.low - 2);
				this.$httpBlock.get(this.$httpUrl+"/blockexplorerapi/queryBlockInfo?number="+previous)
				.then(res => {
					this.previousBlockInfo = res;
				}).catch(err => {
					console.log("err "+err)
				})


				var previous2 = Number(this.channelBlockInfo.data.height.low - 3);
				this.$httpBlock.get(this.$httpUrl+"/blockexplorerapi/queryBlockInfo?number="+previous2)
				.then(res => {
					this.previousBlockInfo2 = res;
				}).catch(err => {
					console.log("err "+err)
				})

			}).catch(err => {
				console.log("err "+err)
			})

			//this.getHash();
		},
		methods:{
			queryInfo(hash){
				this.open = true
				this.$httpBlock.get(this.$httpUrl+"/blockexplorerapi/queryBlockBuHash?hash="+hash)
				.then(res => {
					this.blockInfoHash = res;
				}).catch(err => {
					console.log("err "+err)
				})
			},

			getHash(){
				var number = Number(this.channelBlockInfo.data.height.low );
				this.$httpBlock.get(this.$httpUrl+"/blockexplorerapi/queryBlockInfo?number="+number)
				.then(res => {
					this.blockInfo = res;
				}).catch(err => {
					console.log("err "+err)
				})
			},

			queryBlockByNum(){
				this.queryBlockByNumDrawer = true;
			}

		},
	}
</script>

<style>
</style>

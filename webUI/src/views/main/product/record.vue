<template>
	<div class="app-container">
		<el-divider>미처리 업무</el-divider>
		<el-table :data="taskLists">
			<el-table-column type="selection" width="55" align="center" />
			<el-table-column label="작업번호" prop="taskId" />
			<el-table-column label="작업 제목" prop="taskName" />
			<el-table-column label="게시자" prop="leaderName" />
			<el-table-column label="시간" prop="time" />
			<el-table-column label="내용" prop="taskRemark" />
			<el-table-column label="조작" align="center" class-name="small-padding fixed-width">
				<template slot-scope="scope">
					<el-button size="mini" type="text" @click="addWorkContent(scope.row)">작업 내용을 보고하다</el-button>

				</template>
			</el-table-column>
		</el-table>

		<el-dialog center title="작업 내용을 보고하다" :visible.sync="workContentDialog" width="700px" append-to-body>
			<el-form ref="form" label-width="80px" :model="recordForm">
			<el-row>
				<el-col :span="12">
					<el-form-item label="작업ID" prop="nickName"><el-input :disabled="true" v-model="taskInfo.taskId" placeholder="입력하세요" /></el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="작업 제목" prop="nickName"><el-input :disabled="true" v-model="taskInfo.taskName" placeholder="입력하세요" /></el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="24">
					<el-form-item label="작업 내용"><el-input type="textarea" v-model="recordForm.workContent" placeholder="입력하세요" /></el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="24">
					<el-form-item label="비고"><el-input type="textarea" v-model="recordForm.remarks" placeholder="입력하세요" /></el-form-item>
				</el-col>
			</el-row>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitOperation">확 인</el-button>
				<el-button @click="cancel">최 소</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
import {queryTaskList} from '../../../post_get/blockchain/product';
export default {
	name: 'Record',
	data() {
		return {
			taskLists:[],
			workContentDialog:false,
			recordForm:{},
			taskInfo:'',
		};
	},
	created(){
		this.taskList();
	},
	methods:{
		submitOperation(){
			const operationArray = []
			const id = new this.$snowFlakeId().generate()
			operationArray.push(id)
			operationArray.push(id)
			operationArray.push(this.taskInfo.cropsId)
			operationArray.push(this.$store.getters.nickName)
			operationArray.push(this.$store.getters.phonenumber)
			operationArray.push(formatDate(new Date()))
			operationArray.push(this.recordForm.workContent)
			operationArray.push(this.recordForm.remarks)
			this.$httpBlock
				.post(this.$httpUrl + '/productapi/createOperation', { operationArray: operationArray })
				.then(res => {
					if (res.status === 200) {
						this.msgSuccess("정보가 블록체인에 성공적으로 업로드되었습니다");
						this.workContentDialog = false
					}
				})
				.catch(err => {
					this.msgError('저장 오류' + err);
				});
		},

		addWorkContent(row){
			this.taskInfo = row
			this.workContentDialog = true
		},

		taskList(){
			queryTaskList().then(res => {
				this.taskLists = res.data
			}).catch(err => {
				this.msgError("작업 받기 실패")
			})
		}
	}

}
</script>

<style>
</style>

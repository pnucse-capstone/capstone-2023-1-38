<template>
	<div class="app-container">
		<el-divider>미처리 업무</el-divider>
		<br />
		<el-table :data="transportList">
			<el-table-column type="selection" width="55" align="center" />
			<el-table-column label="농산물 번호" prop="cropsId" />
			<el-table-column label="농산물" prop="cropsName" />
			<el-table-column label="농산물 소속" prop="farmerDept" />
			<el-table-column label="시간" prop="time" />
			<el-table-column label="수령 상태" prop="retailerReceiveStatus">
				<template slot-scope="scope">
					<el-tag v-show="scope.row.retailerReceiveStatus === null">수령하지 않다</el-tag>
					<el-tag v-show="scope.row.retailerReceiveStatus === 1">수령했다</el-tag>
				</template>
			</el-table-column>
			<el-table-column label="조작" align="center" class-name="small-padding fixed-width">
				<template slot-scope="scope">
					<el-button v-show="scope.row.retailerReceiveStatus === null" size="mini" type="text" @click="receive(scope.row)">수령</el-button>
					<el-button size="mini" type="text" @click="generateQrCode(scope.row)">조회ID 생성</el-button>
					<el-button size="mini" type="text" @click="getTraceId(scope.row)">조회ID 가져오기</el-button>
				</template>
			</el-table-column>
		</el-table>


		<el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
			<el-form ref="form" :model="form" label-width="80px">
				<el-row>
					<el-col :span="12">
						<el-form-item label="검사 결과" prop="nickName"><el-input v-model="form.testingResult" placeholder="검사 결과를 입력하세요" /></el-form-item>
					</el-col>
				</el-row>
				<el-form-item label="비고"><el-input v-model="form.remarks" type="textarea" placeholder="내용을 입력하세요"></el-input></el-form-item>
				<el-row>
					<el-col :span="12">
						<el-form-item label="검사 이미지:">
							<el-upload class="avatar-uploader" :on-change="getFile" :show-file-list="false" :auto-upload="false">
								<img v-if="imageUrl" :src="imageUrl" class="avatar" />
								<i v-else class="el-icon-plus avatar-uploader-icon"></i>
							</el-upload>
						</el-form-item>
					</el-col>
				</el-row>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="createMaching">확 인</el-button>
				<el-button @click="cancel">최 소</el-button>
			</div>
		</el-dialog>

		<el-dialog center title="운송 연락" :visible.sync="noticeDetaiDialog" width="500px" append-to-body>
			<el-form ref="form" :model="trasportForm" label-width="80px">
				<el-row>
					<el-col :span="24">
						<el-form-item label="기사 선택" prop="nickName">
							<el-select v-model="trasportForm.userName" placeholder="선택해 주세요">
								<el-option v-for="user in driverList" :key="user.userName" :label="user.nickName" :value="user.userName"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="24">
						<el-form-item label="가공 공장" prop="nickName">
							<el-select v-model="trasportForm.deptId" placeholder="선택해 주세요">
								<el-option v-for="dept in factoryList" :key="dept.deptId" :label="dept.deptName" :value="dept.deptId"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-form-item label="비고"><el-input v-model="trasportForm.remarks" type="textarea" placeholder="입력하세요"></el-input></el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="addNoticeTrasport">확 인</el-button>
				<el-button @click="cancel">최 소</el-button>
			</div>
		</el-dialog>

		<el-dialog center title="농산물 조회ID" :visible.sync="traceIdDialog" width="500px" append-to-body>
			<json-viewer :value="retailerInfo.product_id" expand-depth=5 copyable boxed></json-viewer>
		</el-dialog>

	</div>
</template>

<script>
import { listDept, getDept, delDept, addDept, updateDept, listDeptExcludeChild } from '@/get_post/system/dept';
import {addTransport, listCrops,changeInToOut} from '../../../post_get/blockchain/material';
import Treeselect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import {uplodImagesBase64,getAllDriverByDeptId,getFactoryByDeptId} from '../../../post_get/blockchain/farmer';
import {queryCropsList } from '../../../post_get/blockchain/product';
import {updateReceiveStatus } from '../../../post_get/blockchain/retailer';
import QrCode from 'qrcodejs2'
export default {
	name: 'Dept',
	components: { Treeselect },
	data() {
		return {
			imageUrl:'',
			noticeDetaiDialog: false,
			// 遮罩层
			loading: true,
			// 显示搜索条件
			showSearch: true,
			// 表格树数据
			deptList: [],
			// 部门树选项
			deptOptions: [],
			// 弹出层标题
			title: '',
			// 是否显示弹出层
			open: false,
			// 状态数据字典
			statusOptions: [],
			// 查询参数
			queryParams: {
				deptName: undefined,
				status: undefined
			},
			// 表单参数
			form: {},

			transportList: [],
			cityDetail: '',
			lng: Number,
			lat: Number,
			imgBase64:'',
			TestingPhotoUrl:'',
			checkInfo:'',
			driverList:[],
			noticeTransportDialog:false,
			trasportForm:{},
			factoryList:[],
			traceIdDialog:false,
			retailerInfo:''
		};
	},
	created() {
		this.getList();
	},

	mounted() {
		//this.qrcode();
	},

	methods: {
	  //生成产品溯源码
    generateQrCode(productInfo){
      let qrcode = new QRCode('qrcode', {
        width: 132,
        height: 132,
        text: 'http://1.117.208.193:12011/#/login?redirect=%2Findex', // 需要二维码跳转的地址
        colorDark: "yellow", //前景色
        colorLight: "green", //背景色
      })
    },

		//获取产品溯源ID
		getTraceId(row){
			const retailerArray = []
			retailerArray.push(row.cropsId)
			retailerArray.push(this.$store.getters.name)
			this.$httpBlock
				.get(this.$httpUrl + '/retailerapi/queryRetailerByCropsId?cropsId='+row.cropsId+'&retailerId='+this.$store.getters.name)
				.then(res => {
          if (res.data.length < 1){
            this.msgError("먼저 화물 수령하고 조회ID를 받아 주십시오");
          }else {
            this.traceIdDialog = true
            this.retailerInfo = res.data[0].Record;
          }

				})
				.catch(err => {
					this.msgError('저장 오류' + err);
				});
		},

		//签收
		receive(row){
			const retailerArray = []
			const id = new this.$snowFlakeId().generate();
			retailerArray.push(id)
			retailerArray.push(id)
			retailerArray.push(row.cropsId)
			retailerArray.push(this.$store.getters.nickName)
			retailerArray.push(this.$store.getters.name)
			retailerArray.push(this.$store.getters.phonenumber)
			retailerArray.push(this.$store.getters.deptName)
			retailerArray.push('판매자')
			this.$confirm('수령 확인하시겠습니까?', '수령', {
				confirmButtonText: '확인',
				cancelButtonText: '취소',
				type: 'warning'
			})
				.then(() => {
					this.$httpBlock
						.post(this.$httpUrl + '/retailerapi/createRetailer', { retailerArray: retailerArray })
						.then(res => {
							if (res.status === 200) {
								const info = {
									cropsId:row.cropsId,
									factoryId:this.$store.getters.deptId,
								}
								updateReceiveStatus(info).then(() => {
									this.msgSuccess("화물 수령 성공");
									this.getList();
								}).catch(err => {
									this.msgError("화물 수령 실패")
								})
							}
						})
						.catch(err => {
							this.msgError('저장 오류' + err);
						});
			})
				.catch(() => {
					this.$message({
						type: 'info',
						message: '취소'
					});
				});
		},

		addNoticeTrasport(){
			this.trasportForm.cropsId = this.checkInfo.cropsId;
			this.trasportForm.farmerUserName = this.$store.getters.name;
			this.trasportForm.farmerNickName = this.$store.getters.nickName;
			this.trasportForm.time = new Date();
			this.trasportForm.driverId = this.trasportForm.userName;
			this.trasportForm.status = 0;
			this.trasportForm.farmerTel = this.$store.getters.phonenumber;
			this.trasportForm.farmerDept = this.$store.getters.deptName;
			this.trasportForm.factoryId = this.trasportForm.deptId
			addTransport(this.trasportForm)
				.then(res => {
					this.msgSuccess('기사님에게 연락하여 운송에 성공하다');
					this.noticeDetaiDialog = false;
				})
				.catch(err => {
					this.msgError('기사님 연락해서 운송 실패, 오류 발생');
				});
		},

		noticeTransport(row){
			this.checkInfo = row;
			this.noticeDetaiDialog = true
			getAllDriverByDeptId(this.$store.getters.deptId)
				.then(res => {
					this.driverList = res.data;
				})
				.catch(err => {});
			getFactoryByDeptId(123)
				.then(res => {
					this.factoryList = res.data;
				})
		},

		/**
		 * 出库
		 */
		outFactory(row){
			this.checkInfo = row;
			this.open = true
		},

		createMaching(){
		    const checkInfoArray = []
			const id = new this.$snowFlakeId().generate();
			checkInfoArray.push(id)
			checkInfoArray.push(id)
			checkInfoArray.push(this.$store.getters.nickName)
			checkInfoArray.push(this.$store.getters.phonenumber);
			checkInfoArray.push(this.$store.getters.deptName);
			checkInfoArray.push(this.checkInfo.cropsId);
			checkInfoArray.push(this.form.testingResult);
			checkInfoArray.push(this.checkInfo.time);
			checkInfoArray.push(formatDate(new Date()));
			checkInfoArray.push(this.TestingPhotoUrl);
			checkInfoArray.push(this.form.remarks);
			this.$httpBlock
				.post(this.$httpUrl + '/materialapi/createMaching', { checkInfoArray: checkInfoArray })
				.then(res => {
					if (res.status === 200) {
						changeInToOut(this.checkInfo.cropsId).then(res => {
							this.msgSuccess("출고 성공")
							this.getList();
						}).catch(err => {
							this.msgError("출고 실패")
						})
						this.open = false;
					}
				})
				.catch(err => {
					this.msgError('저장 오류' + err);
				});
		},

		getFile(file) {
			this.imageUrl = URL.createObjectURL(file.raw);
			this.getBase64(file.raw).then(res => {
				this.imgBase64 = res;
				const datas = {
					imageBase64: this.imgBase64
				};
				uplodImagesBase64(datas)
					.then(res => {
						this.TestingPhotoUrl = res.msg;
						this.msgSuccess('사진 업로드 성공');
					})
					.catch(err => {
						this.msgError('사진 업로드 실패');
					});
			});
		},

		getBase64(file) {
			return new Promise(function(resolve, reject) {
				const reader = new FileReader();
				let imgResult = '';
				reader.readAsDataURL(file);
				reader.onload = function() {
					imgResult = reader.result;
				};
				reader.onerror = function(error) {
					reject(error);
				};
				reader.onloadend = function() {
					resolve(imgResult);
				};
			});
		},


		getList() {
			queryCropsList(this.$store.getters.deptId).then(res => {
				this.transportList = res.data;
			});
		},
		/** 转换部门数据结构 */
		normalizer(node) {
			if (node.children && !node.children.length) {
				delete node.children;
			}
			return {
				id: node.deptId,
				label: node.deptName,
				children: node.children
			};
		},
		// 字典状态字典翻译
		statusFormat(row, column) {
			return this.selectDictLabel(this.statusOptions, row.status);
		},
		// 取消按钮
		cancel() {
			this.open = false;
			this.reset();
		},
		// 表单重置
		reset() {
			this.form = {
				deptId: undefined,
				parentId: undefined,
				deptName: undefined,
				orderNum: undefined,
				leader: undefined,
				phone: undefined,
				email: undefined,
				status: '0'
			};
			this.resetForm('form');
		},
		/** 搜索按钮操作 */
		handleQuery() {
			this.getList();
		},
		/** 重置按钮操作 */
		resetQuery() {
			this.resetForm('queryForm');
			this.handleQuery();
		},
		/** 新增按钮操作 */
		handleAdd(row) {
			this.reset();
			if (row != undefined) {
				this.form.parentId = row.deptId;
			}
			this.open = true;
			this.title = '부서 추가';
			listDept().then(response => {
				this.deptOptions = this.handleTree(response.data, 'deptId');
			});
		},
		/** 修改按钮操作 */
		handleUpdate(row) {
			this.reset();
			getDept(row.deptId).then(response => {
				this.form = response.data;
				this.open = true;
				this.title = '주서 수정';
			});
			listDeptExcludeChild(row.deptId).then(response => {
				this.deptOptions = this.handleTree(response.data, 'deptId');
			});
		},
		/** 提交按钮 */
		submitForm: function() {
			this.$refs['form'].validate(valid => {
				if (valid) {
					if (this.form.deptId != undefined) {
						updateDept(this.form).then(response => {
							if (response.code === 200) {
								this.msgSuccess('수정 성공');
								this.open = false;
								this.getList();
							}
						});
					} else {
						addDept(this.form).then(response => {
							if (response.code === 200) {
								this.msgSuccess('추가 성공');
								this.open = false;
								this.getList();
							}
						});
					}
				}
			});
		},
		/** 删除按钮操作 */
		handleDelete(row) {
			this.$confirm('이 데이터를 삭제하시겠습니까?', '경고', {
				confirmButtonText: '확인',
				cancelButtonText: '취소',
				type: 'warning'
			})
				.then(function() {
					return delDept(row.deptId);
				})
				.then(() => {
					this.getList();
					this.msgSuccess('삭제 성공');
				})
				.catch(function() {});
		}
	}
};
</script>
<style>
.avatar-uploader .el-upload {
	border: 1px dashed #d9d9d9;
	border-radius: 6px;
	cursor: pointer;
	position: relative;
	overflow: hidden;
}
.avatar-uploader .el-upload:hover {
	border-color: #409eff;
}
.avatar-uploader-icon {
	font-size: 28px;
	color: #8c939d;
	width: 178px;
	height: 178px;
	line-height: 178px;
	text-align: center;
}
.avatar {
	width: 300px;
	height: 270px;
	display: block;
}

.image {
	width: 80%;
	display: block;
}
</style>

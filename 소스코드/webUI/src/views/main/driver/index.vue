<template>
	<div class="app-container">
		<el-divider>미처리 업무</el-divider>

		<br />
		<el-table :data="transportList">
			<el-table-column type="selection" width="55" align="center" />
			<el-table-column label="화물번호" prop="cropsId" />
			<el-table-column label="통지인" prop="farmerNickName" />
			<el-table-column label="소속" prop="farmerDept" />
			<el-table-column label="시간" prop="time" />
			<el-table-column label="비고" prop="remarks" />
			<el-table-column label="상테" prop="status">
				<template slot-scope="scope">
					<el-tag v-if="scope.row.status === 0">미운송</el-tag>
					<el-tag v-if="scope.row.status === 1">운송중</el-tag>
					<el-tag v-if="scope.row.status === 2">운송완료</el-tag>
				</template>
			</el-table-column>
			<el-table-column label="조작" align="center" class-name="small-padding fixed-width">
				<template slot-scope="scope">
					<el-button v-show="scope.row.status === 0" size="mini" type="text" @click="startTransport(scope.row)">운송시작</el-button>
					<el-button v-show="scope.row.status === 1" size="mini" type="text" @click="transportLocation(scope.row)">위치</el-button>
					<el-button v-show="scope.row.status === 1" size="mini" type="text" @click="isOk(scope.row)">배송원료</el-button>
<!--					<el-button size="mini" type="text" @click="isOk(scope.row)">物流追踪</el-button>-->
				</template>
			</el-table-column>
		</el-table>

		<div v-show="false" id="tip"></div>
		<div v-show="false" id="lng"></div>
		<div v-show="false" id="lat"></div>


    <el-dialog center title="주소를 입력하세요" :visible.sync="addressVisible" width="700px" append-to-body>
      <el-form ref="form" label-width="80px" >
        <el-form-item label="주소" prop="nickName">
          <el-input v-model="targetAddress" placeholder="주소를 입력하세요" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" type="primary" @click="submit">확 인</el-button>
      </div>
    </el-dialog>
	</div>
</template>

<script>
import { listDept, getDept, delDept, addDept, updateDept, listDeptExcludeChild } from '@/post_get/system/dept';
import { listTransport, saveAndUpdateTransportInfoToDb ,updateTransportStatus} from '../../../post_get/blockchain/driver';
import Treeselect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
export default {
	name: 'Dept',
	components: { Treeselect },
	data() {
		return {
      targetAddress:null,
      addressVisible:false,
			noticeDetaiDialog: false,

      // Loading overlay
			loading: true,

      // Show search conditions
			showSearch: true,

      // Table tree data
			deptList: [],

      // Department tree options
			deptOptions: [],

      // Popup title
			title: '',

      // Whether to show the popup
			open: false,

      // Status options dictionary
			statusOptions: [],

      // Query parameters
			queryParams: {
				deptName: undefined,
				status: undefined
			},
			// 表单参数
      // Form parameters

			form: {},

			transportList: [],
			cityDetail: '',
			lng: Number,
			lat: Number,
      transportArray: [],
      cropsId: null,
		};
	},
	created() {
		this.getList();
	},

	mounted() {
		AMap.plugin('AMap.Geolocation', function() {
			var geolocation = new AMap.Geolocation({

        // Whether to use high-accuracy positioning, default: true
				enableHighAccuracy: true,

        // Set the timeout for positioning, default: Infinity
				timeout: 10000,

        // The offset of the positioning button's dock position, default: Pixel(10, 20)
				buttonOffset: new AMap.Pixel(10, 20),

        // Adjust the map view to make the positioning position and accuracy range visible after
				zoomToAccuracy: true,

        // The position of the positioning button, 'RB' represents right bottom
				buttonPosition: 'RB'
			});
			geolocation.getCurrentPosition();
			AMap.event.addListener(geolocation, 'complete', onComplete);
			AMap.event.addListener(geolocation, 'error', onError);
			function onComplete(data) {
				this.cityDetail = data.addressComponent.province + data.addressComponent.city + data.addressComponent.district + data.addressComponent.township;
				this.lng = data.position.lng;
				this.lat = data.position.lat;
				const str = [];
				str.push(data.position.lng);
				str.push(data.position.lat);
				document.getElementById('lng').innerHTML = data.position.lng;
				document.getElementById('lat').innerHTML = data.position.lat;
				document.getElementById('tip').innerHTML = this.cityDetail;
			}
			function onError(data) {
				console.log('위치 오류   ' + JSON.stringify(data));
			}
		});
	},

	methods: {
		isOk(row){
			const transportArray = [];
			const id = new this.$snowFlakeId().generate();
			transportArray.push(id);
			transportArray.push(id);
			transportArray.push(this.$store.getters.name);
			transportArray.push(this.$store.getters.nickName);
			transportArray.push(this.$store.getters.phonenumber);
			transportArray.push(this.$store.getters.deptName);
			transportArray.push(formatDate(new Date()));
			transportArray.push(document.getElementById('tip').innerHTML);
			transportArray.push(row.cropsId);
			transportArray.push('도착');
			this.$confirm('배송완료 되었는지 확인부탁드립니다', '배송', {
				confirmButtonText: '확인',
				cancelButtonText: '취소',
				type: 'warning'
			})
				.then(() => {
					this.$httpBlock
						.post(this.$httpUrl + '/driverapi/createTransport', { transportArray: transportArray })
						.then(res => {
							if (res.status === 200) {
								this.msgSuccess('정보가 블록체인에 성공적으로 업로드되었습니다');
								this.growDialog = false;
								const transportInfo = {
									lng: Number(document.getElementById('lng').innerHTML),
									lat: Number(document.getElementById('lat').innerHTML),
									cropsId: row.cropsId,
									status: 2,
									id: row.id,
									outFactoryStatus:0
								};
								saveAndUpdateTransportInfoToDb(transportInfo)
									.then(res => {
										this.getList();
									})
									.catch(err => {

									});
							}
						})
						.catch(err => {
							this.msgError('저장 오류 ' + err);
						});
					this.$message({
						type: 'success',
						message: '배송 성공!'
					});
				})
				.catch(() => {
					this.$message({
						type: 'info',
						message: '취소'
					});
				});
		},

    submit(){
      this.$confirm(this.targetAddress, '위치', {
        confirmButtonText: '확 인',
        cancelButtonText: '최 소',
        type: 'warning'
      })
        .then(() => {
          this.transportArray[7] = this.targetAddress;
          this.$httpBlock
            .post(this.$httpUrl + '/driverapi/createTransport', { transportArray: this.transportArray })
            .then(res => {
              if (res.status === 200) {
                this.msgSuccess('정보가 블록체인에 성공적으로 업로드되었습니다');
                this.addressVisible = false;
                const transportInfo = {
                  lng: Number(null),
                  lat: Number(null),
                  cropsId: this.cropsId,
                  status: 1,
                };
                saveAndUpdateTransportInfoToDb(transportInfo)
                  .then(res => {
                    this.getList();
                  })
                  .catch(err => {

                  });
              }
            })
            .catch(err => {
              this.msgError('저장 오류 ' + err);
            });

        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '위치 취소'
          });
        });
    },

		transportLocation(row) {
      this.addressVisible = true;
			const id = new this.$snowFlakeId().generate();
			this.transportArray.push(id);
      this.transportArray.push(id);
      this.transportArray.push(this.$store.getters.name);
      this.transportArray.push(this.$store.getters.nickName);
      this.transportArray.push(this.$store.getters.phonenumber);
      this.transportArray.push(this.$store.getters.deptName);
      this.transportArray.push(formatDate(new Date()));
      this.transportArray.push(this.targetAddress);
      this.transportArray.push(row.cropsId);
      this.transportArray.push('도중');
      this.cropsId = row.cropsId;
		},
		/**
		 * @param {Object} row开始配送
		 */
		startTransport(row) {
			const transportArray = [];
			const id = new this.$snowFlakeId().generate();
			transportArray.push(id);
			transportArray.push(id);
			transportArray.push(this.$store.getters.name);
			transportArray.push(this.$store.getters.nickName);
			transportArray.push(this.$store.getters.phonenumber);
			transportArray.push(this.$store.getters.deptName);
			transportArray.push(formatDate(new Date()));
			transportArray.push(document.getElementById('tip').innerHTML);
			transportArray.push(row.cropsId);
			transportArray.push('출발');
			this.$confirm('운송을 시작하시겠습니까?', '제시', {
				confirmButtonText: '확인',
				cancelButtonText: '취소',
				type: 'warning'
			})
				.then(() => {
					this.$httpBlock
						.post(this.$httpUrl + '/driverapi/createTransport', { transportArray: transportArray })
						.then(res => {
							if (res.status === 200) {
								this.msgSuccess('정보가 블록체인에 성공적으로 업로드되었습니다');
								this.growDialog = false;
								const transportInfo = {
									lng: Number(document.getElementById('lng').innerHTML),
									lat: Number(document.getElementById('lat').innerHTML),
									cropsId: row.cropsId,
									id: row.id,
									status: 1,
								};
								saveAndUpdateTransportInfoToDb(transportInfo)
									.then(res => {

										this.getList();
									})
									.catch(err => {

									});
							}
						})
						.catch(err => {
							this.msgError('저장 오류 ' + err);
						});
					this.$message({
						type: 'success',
						message: '운송 시작'
					});
				})
				.catch(() => {
					this.$message({
						type: 'info',
						message: '운송 취소'
					});
				});
		},

		getList() {
			listTransport(this.$store.getters.name).then(res => {
				this.transportList = res.data;
			});
		},

    /** Convert department data structure */
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

    // Dictionary state dictionary translation
		statusFormat(row, column) {
			return this.selectDictLabel(this.statusOptions, row.status);
		},
    // Cancel button
		cancel() {
			this.open = false;
			this.reset();
		},
    // Form reset
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
    /** Search button operation */
		handleQuery() {
			this.getList();
		},
    /** Reset button operation */
		resetQuery() {
			this.resetForm('queryForm');
			this.handleQuery();
		},
    /** Add button operation */
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
    /** Update button operation */
		handleUpdate(row) {
			this.reset();
			getDept(row.deptId).then(response => {
				this.form = response.data;
				this.open = true;
				this.title = '부서 수정';
			});
			listDeptExcludeChild(row.deptId).then(response => {
				this.deptOptions = this.handleTree(response.data, 'deptId');
			});
		},
    /** Submit button */
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
    /** Delete button operation */
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

<template>
  <div style="height: 4000px;overflow: auto;">
    <el-row :gutter="100">
      <el-col :span="24">
        <avue-data-tabs :option="option"></avue-data-tabs>
        <br />
        <div>
          <div>
            <el-row>
              <el-col :span="60">Block number：{{ blockInfo.data.header.number }}</el-col>
            </el-row>
            <br />
            <el-row>
              <el-col :span="60">
                Predecessor Hash：
                <el-link @click="queryInfo(blockInfo.data.header.previous_hash)" type="success">{{ blockInfo.data.header.previous_hash }}<span style="color: red">(클릭하여 블록체인 정보 보기)</span></el-link>
              </el-col>
            </el-row>
            <br />
            <el-row>
              <el-col :span="60">
                Data Hash：
                <el-link :underline="false" type="success">{{ blockInfo.data.header.data_hash }}</el-link>
              </el-col>
            </el-row>
          </div>
          <div align="center">
            <el-divider content-position="left"><i class="el-icon-top"></i></el-divider>
          </div>
          <div>
            <el-row>
              <el-col :span="60">Block number：{{ previousBlockInfo.data.header.number }}</el-col>
            </el-row>
            <br />
            <el-row>
              <el-col :span="60">
                Predecessor Hash：
                <el-link @click="queryInfo(previousBlockInfo.data.header.previous_hash)" type="success">{{ previousBlockInfo.data.header.previous_hash }}<span style="color: red">(클릭하여 블록체인 정보 보기)</span></el-link>
              </el-col>
            </el-row>
            <br />
            <el-row>
              <el-col :span="60">
                Data Hash：
                <el-link :underline="false" type="success">{{ previousBlockInfo.data.header.data_hash }}</el-link>
              </el-col>
            </el-row>
          </div>
          <div align="center">
            <el-divider content-position="left"><i class="el-icon-top"></i></el-divider>
          </div>

          <div>
            <el-row>
              <el-col :span="60">Block number：{{ previousBlockInfo2.data.header.number }}</el-col>
            </el-row>
            <br />
            <el-row>
              <el-col :span="60">
                Predecessor Hash：
                <el-link @click="queryInfo(previousBlockInfo2.data.header.previous_hash)" type="success">
                  {{ previousBlockInfo2.data.header.previous_hash }}<span style="color: red">(클릭하여 블록체인 정보 보기)</span>
                </el-link>
              </el-col>
            </el-row>
            <br />
            <el-row>
              <el-col :span="60">
                Data Hash：
                <el-link :underline="false" type="success">{{ previousBlockInfo2.data.header.data_hash }}</el-link>
              </el-col>
            </el-row>
          </div>
        </div>
        <br />
        <br />
      </el-col>
      <el-col :span="24">
        <div>
          <el-input style="width: 50%;" v-model="traceId" placeholder="농산물 조회번호를 입력하세요"></el-input>
          &nbsp;&nbsp;&nbsp;
          <el-button type="success" @click="query">조회</el-button>
          <br />
        </div>
        <br />
        <br />
        <div>
          <el-row>
            <el-col :span="6">
              <el-form>
                <el-form-item><span style="color: #13CE66;">가공 공장 정보</span></el-form-item>
                <el-form-item label="가공 공장:">{{ productData.factory }}</el-form-item>
                <el-form-item label="공장 책임자:">{{ productData.leader }}</el-form-item>
                <el-form-item label="전화번호:">{{ productData.leader_tel }}</el-form-item>
                <el-form-item label="중량:">{{ productData.Net_Content }}</el-form-item>
                <el-form-item label="가공 작업장:">{{ productData.workshop }}</el-form-item>
                <el-form-item label="가공시간：">{{ productData.work_hours }}</el-form-item>
                <el-form-item label="보존 방법：">{{ productData.keep_mathod }}</el-form-item>
                <el-form-item label="먹는 방법：">{{ productData.cooking_recommend }}</el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form>
                <el-form-item><span style="color: #13CE66;">판매자 정보</span></el-form-item>
                <el-form-item label="농산물 ID:">{{ retailerInfo.product_id }}</el-form-item>
                <el-form-item label="농산물:">{{ productData.product_name }}</el-form-item>
                <el-form-item label="판매장소:">{{ retailerInfo.retailer }}</el-form-item>
                <el-form-item label="책임자:">{{ retailerInfo.retailer_name }}</el-form-item>
                <el-form-item label="전화번호">{{ retailerInfo.retailer_tel }}</el-form-item>
              </el-form>
            </el-col>
            <el-col :span="12">
              <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane label="재배 과정" name="1">

                  <el-row :gutter="8">
                    <el-col :span="24" v-for="(info, index) in cropsProcessDetailsArray" :key="index" style="padding-top: 10px;">
                      <el-card style="border-color: #42b983;">
                        <el-col :span="12">
                          <el-row>
                            <el-col :span="24">생장 상황：{{ info.grow_status }}</el-col>

                          </el-row>
                          <br />
                          <el-row>
                            <el-col :span="24">일조 상황：{{ info.illumination_status }}</el-col>

                          </el-row>
                          <br />
                          <el-row>
                            <el-col :span="24">수분：{{ info.water_content }}</el-col>
                          </el-row>
                          <br />
                          <el-row>
                            <el-col :span="24">온도：{{ info.temperature }}</el-col>
                          </el-row>
                          <br />
                          <el-row>
                            <el-col :span="24">기록시간：{{ info.record_time }}</el-col>
                          </el-row>
                          <br />
                          <el-row>
                            <el-col :span="24">비고：{{ info.remarks }}</el-col>
                          </el-row>
                        </el-col>
                        <el-col :span="12"><img style="width: 100%;height: 11.05rem;" :src="info.crops_grow_photo_url" /></el-col>
                      </el-card>
                    </el-col>
                    <br />
                  </el-row>
                </el-tab-pane>
                <el-tab-pane label="물류정보" name="2">
                  <div class="block">
                    <el-timeline v-for="(detail,index) in cropsDriverArray">
                      <el-timeline-item :timestamp="detail.transport_to_chain_time" placement="top" >
                        <el-card>
                          <h4 style="color: red;">경유지:{{detail.transport_to_address}}</h4>
                          <p>물류ID:{{detail.transport_id}}</p>
                          <p>기사님:{{detail.driver_name}}</p>
                          <p>전화번호:{{detail.driver_tel}}</p>
                          <p>비고:{{detail.remarks}}</p>
                        </el-card>
                      </el-timeline-item>
                    </el-timeline>
                  </div>
                </el-tab-pane>
                <el-tab-pane label="원료 공장-품질검사" name="3">
                  <el-card style="border-color: #42b983;">
                    <el-col :span="12">
                      <el-row>
                        <el-col :span="24">책임자：{{ machingInfo.leader }}</el-col>
                      </el-row>
                      <br /><br />
                      <el-row>
                        <el-col :span="24">전화번호：{{ machingInfo.leader_tel }}</el-col>
                      </el-row>
                      <br /><br />
                      <el-row>
                        <el-col :span="24">원료 공장：{{ machingInfo.factory_name }}</el-col>
                      </el-row>
                      <br /><br />
                      <el-row>
                        <el-col :span="24">공장입고 시간：{{ machingInfo.in_factory_time }}</el-col>
                      </el-row>
                      <br /><br />
                      <el-row>
                        <el-col :span="24">공장출하 시간：{{ machingInfo.out_factory_time }}</el-col>
                      </el-row>
                      <br /><br />
                      <el-row>
                        <el-col :span="24">검사 결과：{{ machingInfo.testing_result }}</el-col>
                      </el-row>
                      <br /><br />
                      <el-row>
                        <el-col :span="24">비고：{{ machingInfo.remarks }}</el-col>
                      </el-row>
                      <br />
                    </el-col>
                    <el-col :span="12"><img style="width: 100%;height: 21.05rem;" :src="machingInfo.testing_photo_url" /></el-col>
                  </el-card>
                </el-tab-pane>
                <el-tab-pane label="가공작업" name="4">
                  <el-row :gutter="8" >
                    <el-col :span="24" v-for="(info,index) in operationArray" :key="index" style="padding-top: 10px;">
                      <el-card style="border-color: #42b983;">
                        <el-row>
                          <el-col :span="24">작업ID：{{ info.operation_id }}</el-col>
                        </el-row>
                        <br>
                        <el-row>
                          <el-col :span="24">작업 직원：{{ info.operation_people_name }}</el-col>
                        </el-row>
                        <br>
                        <el-row>
                          <el-col :span="24">전화번호：{{ info.operation_people_tel }}</el-col>
                        </el-row>
                        <br>
                        <el-row>
                          <el-col :span="24">작업 시간：{{ info.time }}</el-col>
                        </el-row>
                        <br>
                        <el-row>
                          <el-col :span="24">작업 내용：{{ info.work_content }}</el-col>
                        </el-row>
                      </el-card>
                    </el-col>
                    <br>
                  </el-row>
                </el-tab-pane>
                <el-tab-pane label="농산물 정보" name="5">
                  <el-card style="border-color: #42b983;">
                    <el-row><el-col :span="24">농산물：{{ cropsDetails.crops_name }}</el-col></el-row><br><br>
                    <el-row><el-col :span="24">재배 농가：{{ cropsDetails.farmer_name }}</el-col></el-row><br><br>
                    <el-row><el-col :span="24">전화번호：{{ cropsDetails.farmer_tel }}</el-col></el-row><br><br>
                    <el-row><el-col :span="24">주소：{{ cropsDetails.address }}</el-col></el-row><br><br>
                    <el-row><el-col :span="24">재배 방식：{{ cropsDetails.plant_mode }}</el-col></el-row><br><br>
                    <el-row><el-col :span="24">연도：{{ cropsDetails.year }}</el-col></el-row><br><br>
                    <el-row><el-col :span="24">온실 재배：{{ cropsDetails.bagging_status }}</el-col></el-row><br><br>
                    <el-row><el-col :span="24">비료：{{ cropsDetails.fertilizer_name }}</el-col></el-row><br><br>
                    <el-row><el-col :span="24">육묘 주기：{{ cropsDetails.grow_seedlings_cycle }}</el-col></el-row><br><br>
                    <el-row><el-col :span="24">관개 주기：{{ cropsDetails.irrigation_cycle }}</el-col></el-row><br><br>
                    <el-row><el-col :span="24">제초 주기：{{ cropsDetails.weed_cycle }}</el-col></el-row><br><br>
                    <el-row><el-col :span="24">거름 주기：{{ cropsDetails.apply_fertilizer_cycle }}</el-col></el-row><br><br>
                    <el-row><el-col :span="24">등록일자：{{ cropsDetails.register_time }}</el-col></el-row><br><br>
                    <el-row><el-col :span="24">비고：{{ cropsDetails.remarks }}</el-col></el-row><br>
                  </el-card>
                </el-tab-pane>
              </el-tabs>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
    <BlockInfo ref="getBlockInfoRef"></BlockInfo>
  </div>
</template>

<script>
import axios from 'axios';
import BlockInfo from "./Information.vue";

export default {
  components: {BlockInfo},
  data() {
    return {
      queryBlockByNumDrawer: false,
      channelBlockInfo: '',
      blockInfo: '',
      previousBlockInfo: '',
      previousBlockInfo2: '',
      open: false,
      blockInfoHash: '',
      option: {
        data: []
      },
      traceId: '',
      retailerInfo: '',
      productData: '',
      activeName: '1',
      cropsProcessDetailsArray: [],
      cropsDriverArray:[],
      machingInfo:'',
      operationArray:[],
      cropsDetails:''
    };
  },
  created() {
    this.$httpBlock
      .get(this.$httpUrl + '/blockexplorerapi/channelBlockInfo')
      .then(res => {
        this.channelBlockInfo = res;
        let data1 = {
          title: 'Block Height',
          subtitle: 'real time',
          count: res.data.height.low,
          text: 'Current Block Height',
          color: 'rgb(27, 201, 142)',
          key: '高'
        };
        this.option.data.push(data1);
        //지금 block 정보
        var number = Number(this.channelBlockInfo.data.height.low - 1);
        this.$httpBlock
          .get(this.$httpUrl + '/blockexplorerapi/queryBlockInfo?number=' + number)
          .then(res => {
            this.blockInfo = res;
          })
          .catch(err => {
            console.log('err ' + err);
          });

        //block 2
        var previous = Number(this.channelBlockInfo.data.height.low - 2);
        this.$httpBlock
          .get(this.$httpUrl + '/blockexplorerapi/queryBlockInfo?number=' + previous)
          .then(res => {
            this.previousBlockInfo = res;
          })
          .catch(err => {
            console.log('err ' + err);
          });

        //block 3
        var previous2 = Number(this.channelBlockInfo.data.height.low - 3);
        this.$httpBlock
          .get(this.$httpUrl + '/blockexplorerapi/queryBlockInfo?number=' + previous2)
          .then(res => {
            this.previousBlockInfo2 = res;
          })
          .catch(err => {
            console.log('err ' + err);
          });
      })
      .catch(err => {
        console.log('err ' + err);
      });

    //this.getHash();
  },
  methods: {
    handleClick() {
      if (this.activeName === '1') {
        this.$httpBlock
          .get(this.$httpUrl + '/farmerapi/queryCropsProcessByCropsId?id=' + this.productData.crops_id)
          .then(res => {
            const array = [];
            for (let i = 0; i < res.data.length; i++) {
              array.push(res.data[i].Record);
            }
            this.cropsProcessDetailsArray = array;
          })
          .catch(err => {});
      }
      if(this.activeName === '2'){
        this.$httpBlock
          .get(this.$httpUrl + '/driverapi/queryTransportByCropsId?id='+this.productData.crops_id)
          .then(res => {
            const array = [];
            this.len = res.data.length
            for (let i = 0; i < res.data.length; i++) {
              array.push(res.data[i].Record);
            }
            this.cropsDriverArray = array
          })
          .catch(err => {});
      }
      if(this.activeName === '3'){
        this.$httpBlock
          .get(this.$httpUrl + '/materialapi/queryMachiningByCropsId?id='+this.productData.crops_id)
          .then(res => {
            this.machingInfo = res.data[0].Record
          })
          .catch(err => {});
      }
      if(this.activeName === '4'){
        this.$httpBlock
          .get(this.$httpUrl + '/productapi/queryOperationByCropsId?id='+this.productData.crops_id)
          .then(res => {
            const array = [];
            for (let i = 0; i < res.data.length; i++) {
              array.push(res.data[i].Record);
            }
            this.operationArray = array;
          })
          .catch(err => {
            this.msgError('조회 오류 ' + err);
          });
      }
      if(this.activeName === '5'){
        this.$httpBlock
          .get(this.$httpUrl + '/farmerapi/queryCropsById?id='+this.productData.crops_id)
          .then(res => {
            this.cropsDetails = res.data;
          })
          .catch(err => {});
      }
    },

    query() {
      this.$httpBlock
        .get(this.$httpUrl + '/retailerapi/queryRetailerById?id=' + this.traceId)
        .then(res => {
          this.retailerInfo = res.data; //零售
          this.$httpBlock
            .get(this.$httpUrl + '/productapi/queryProductInfoByCropsId?id=' + res.data.crops_id)
            .then(res => {
              this.productData = res.data[0].Record; //产品信息
              this.handleClick()
            })
            .catch(err => {
              this.msgError('조회 오류 ' + err);
            });
        })
        .catch(err => {
          console.log('호출 실패 ' + JSON.stringify(err));
          //call failure
        });

    },

// queryInfo(hash): 지정된 해시값에 해당하는 블록 정보를 조회하는 함수
    queryInfo(hash) {
      this.open = true;
      this.$httpBlock
        .get(this.$httpUrl + '/blockexplorerapi/queryBlockBuHash?hash=' + hash)
        .then(res => {
          this.blockInfoHash = res;
          this.$refs.getBlockInfoRef.init(res);
        })
        .catch(err => {
          console.log('err ' + err);
        });
    },

// getHash(): 블록의 높이를 기반으로 블록 정보를 조회하는 함수
    getHash() {
      var number = Number(this.channelBlockInfo.data.height.low);
      this.$httpBlock
        .get(this.$httpUrl + '/blockexplorerapi/queryBlockInfo?number=' + number)
        .then(res => {
          this.blockInfo = res;
        })
        .catch(err => {
          console.log('err ' + err);
        });
    },

    queryBlockByNum() {
      this.queryBlockByNumDrawer = true;
    }
  }
};
</script>

<style lang="scss" scoped>
#app {
  width: 100%;
  padding: 20px 20px 10px;
  border-radius: 4px;
  margin: 0px auto;
  background: #ffffff;

  box-sizing: border-box;
}

.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}

#input-box {
  display: block;
  width: 100%;
  height: 46px;
  margin-bottom: 15px;
  border: none;
  border-radius: 6px;
  background: #add8e6;
  padding: 0 5px;
  text-align: right;
  font-size: 20px;
  color: #ffffff;
  font-weight: bold;
  letter-spacing: 1px;
}
</style>

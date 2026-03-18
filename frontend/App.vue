<script>
export default {
  onLaunch: function() {
    console.log('App Launch')
  },
  onShow: function() {
    console.log('App Show')
  },
  onHide: function() {
    console.log('App Hide')
  }
}
</script>

<style lang="scss">
/*每个页面公共 css */
@import '@/uni_modules/uni-scss/index.scss';

page {
  background-color: #F8F8F8;
  font-size: 28rpx;
  line-height: 1.5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

/* 通用样式 */
.container {
  padding: 20rpx;
}

.card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.06);
}

.btn-primary {
  background: linear-gradient(90deg, #FFD101 0%, #FFC400 100%);
  color: #333;
  border: none;
  border-radius: 40rpx;
  padding: 20rpx 60rpx;
  font-size: 32rpx;
  font-weight: 600;
}

.btn-primary:active {
  opacity: 0.8;
}

.text-primary {
  color: #FFD101;
}

.text-gray {
  color: #999;
}

.flex {
  display: flex;
}

.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.flex-between {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.flex-wrap {
  display: flex;
  flex-wrap: wrap;
}

.flex-1 {
  flex: 1;
}

.mt-2 {
  margin-top: 20rpx;
}

.mb-2 {
  margin-bottom: 20rpx;
}
</style>

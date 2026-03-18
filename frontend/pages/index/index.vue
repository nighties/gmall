<template>
  <view class="container">
    <!-- 搜索栏 -->
    <view class="search-bar card">
      <view class="search-input" @click="goToSearch">
        <text class="iconfont icon-search">🔍</text>
        <text class="placeholder">搜索商品、商家</text>
      </view>
    </view>

    <!-- 轮播图 -->
    <view class="banner card">
      <swiper class="swiper" indicator-dots circular autoplay>
        <swiper-item v-for="(item, index) in banners" :key="index">
          <image :src="item.image" mode="aspectFill" class="banner-image"></image>
        </swiper-item>
      </swiper>
    </view>

    <!-- 分类导航 -->
    <view class="categories card">
      <view class="category-item" v-for="(item, index) in categories" :key="index" @click="goToCategory(item)">
        <view class="category-icon">{{ item.icon }}</view>
        <text class="category-name">{{ item.name }}</text>
      </view>
    </view>

    <!-- 推荐商家 -->
    <view class="shops">
      <view class="section-title">附近推荐</view>
      <view class="shop-item card" v-for="(shop, index) in shops" :key="index" @click="goToShop(shop)">
        <image :src="shop.logo" mode="aspectFill" class="shop-logo"></image>
        <view class="shop-info">
          <view class="shop-name">{{ shop.name }}</view>
          <view class="shop-desc">{{ shop.description }}</view>
          <view class="shop-meta">
            <text class="shop-status" :class="shop.status === 1 ? 'open' : 'closed'">
              {{ shop.status === 1 ? '营业中' : '休息中' }}
            </text>
            <text class="shop-distance">{{ shop.distance }}km</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      banners: [
        { image: 'https://placeholder.com/banner1.png' },
        { image: 'https://placeholder.com/banner2.png' },
        { image: 'https://placeholder.com/banner3.png' }
      ],
      categories: [
        { id: 1, name: '美食', icon: '🍔' },
        { id: 2, name: '外卖', icon: '🛵' },
        { id: 3, name: '酒店', icon: '🏨' },
        { id: 4, name: '电影', icon: '🎬' },
        { id: 5, name: '购物', icon: '🛍️' },
        { id: 6, name: '娱乐', icon: '🎮' }
      ],
      shops: []
    }
  },
  onLoad() {
    this.loadShops()
  },
  onPullDownRefresh() {
    this.loadShops()
    uni.stopPullDownRefresh()
  },
  methods: {
    async loadShops() {
      // TODO: 调用后端 API 获取商家列表
      // 暂时使用模拟数据
      this.shops = [
        {
          id: 1,
          name: '麦当劳',
          logo: 'https://placeholder.com/shop1.png',
          description: '全球知名快餐连锁',
          status: 1,
          distance: 0.5
        },
        {
          id: 2,
          name: '肯德基',
          logo: 'https://placeholder.com/shop2.png',
          description: '炸鸡汉堡专家',
          status: 1,
          distance: 0.8
        },
        {
          id: 3,
          name: '星巴克',
          logo: 'https://placeholder.com/shop3.png',
          description: '高品质咖啡',
          status: 1,
          distance: 1.2
        }
      ]
    },
    goToSearch() {
      uni.navigateTo({
        url: '/pages/search/search'
      })
    },
    goToCategory(item) {
      uni.navigateTo({
        url: `/pages/goods/list?categoryId=${item.id}&categoryName=${item.name}`
      })
    },
    goToShop(shop) {
      // TODO: 跳转到商家详情页
      uni.showToast({
        title: '商家详情页开发中',
        icon: 'none'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  padding: 20rpx;
}

.search-bar {
  .search-input {
    display: flex;
    align-items: center;
    background: #F5F5F5;
    border-radius: 40rpx;
    padding: 20rpx 30rpx;
    
    .icon-search {
      font-size: 32rpx;
      margin-right: 16rpx;
    }
    
    .placeholder {
      color: #999;
      font-size: 28rpx;
    }
  }
}

.banner {
  padding: 0;
  overflow: hidden;
  
  .swiper {
    height: 300rpx;
    
    .banner-image {
      width: 100%;
      height: 100%;
    }
  }
}

.categories {
  display: flex;
  flex-wrap: wrap;
  padding: 30rpx 20rpx;
  
  .category-item {
    width: 33.33%;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 20rpx;
    
    .category-icon {
      font-size: 80rpx;
      margin-bottom: 10rpx;
    }
    
    .category-name {
      font-size: 24rpx;
      color: #666;
    }
  }
}

.shops {
  .section-title {
    font-size: 32rpx;
    font-weight: 600;
    color: #333;
    margin: 30rpx 0 20rpx;
  }
  
  .shop-item {
    display: flex;
    padding: 20rpx;
    
    .shop-logo {
      width: 160rpx;
      height: 160rpx;
      border-radius: 12rpx;
      margin-right: 20rpx;
    }
    
    .shop-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      
      .shop-name {
        font-size: 32rpx;
        font-weight: 600;
        color: #333;
      }
      
      .shop-desc {
        font-size: 24rpx;
        color: #999;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      
      .shop-meta {
        display: flex;
        align-items: center;
        justify-content: space-between;
        
        .shop-status {
          font-size: 20rpx;
          padding: 4rpx 12rpx;
          border-radius: 8rpx;
          
          &.open {
            background: #e8f5e9;
            color: #4caf50;
          }
          
          &.closed {
            background: #ffebee;
            color: #f44336;
          }
        }
        
        .shop-distance {
          font-size: 20rpx;
          color: #999;
        }
      }
    }
  }
}
</style>

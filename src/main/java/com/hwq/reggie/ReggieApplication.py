from flask import Flask
import logging

# 创建 Flask 应用
app = Flask(__name__)

# 配置日志
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

@app.route('/')
def home():
    return "Hello, Reggie Application!"

if __name__ == '__main__':
    logger.info("Successful project launch...")
    app.run(host='0.0.0.0', port=5000)  # 启动 Flask 应用
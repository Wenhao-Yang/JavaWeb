#include "HelloWorldScene.h"
#include <fstream>
#include <iostream>
#include <string>
#include <math.h>

#define create_weight _screenSize.width * 0.0145
#define create_height _screenSize.height * 0.017
#define create_weight1 _screenSize.width * 0.0125
#define create_height1 _screenSize.height * 0.0185
#define create_weight2 _screenSize.width * 0.0020
#define create_height2 _screenSize.height * 0.015
#define DF_LEVEL1  (5000)
#define DF_LEVEL2  (3000)
#define DF_STARTER  (2)

USING_NS_CC;
using namespace std;
CCPoint point1 = ccp(0, 0);
CCPoint point2 = ccp(10, 10);
CCPoint point3 = ccp(0, 0);
CCPoint point4 = ccp(0, 0);
CCPoint point5 = ccp(0, 0);
CCPoint point6 = ccp(0, 0);
CCPoint point7 = ccp(0, 0);
CCPoint point8 = ccp(0, 0);
CCPoint point9 = ccp(0, 0);
double a[10000] = { 0 };
double b[10000] = { 0 };
double c[10000] = { 0 };
double d[10000] = { 0 };
double a1[10000] = { 0 };
double b1[10000] = { 0 };
double c1[10000] = { 0 };
double d1[10000] = { 0 };
int i1 = 0;
int i2 = 0;
int i1_pre = 0;
int num = 0;
double x[6];
double y[6];
float r = 0;
double p1[12];
double p2[12];
int ganx = 0, gany = 0;
int ganx1 = 0, gany1 = 0;
//ofstream fout;
bool way = true;
bool shibie = true;
bool chugan = false;
bool chuganpre = false;
bool way1 = true;
bool way1_chugan = true;
int shuaxin = 0;

HelloWorldScene::HelloWorldScene() {
	_screenSize = CCDirector::sharedDirector()->getWinSize();//�趨��Ļ�Ĵ�С

	//CCSprite * bg = CCSprite::create("white.png");//����һ����������
	//bg->setPosition(ccp(_screenSize.width * 0.5f, _screenSize.height * 0.5f));//�������ĵ�
	//this->addChild(bg,-1);

	CCSize s = CCDirector::sharedDirector()->getWinSize();
	CCLayer *layer = CCLayerColor::create(ccc4(255, 255, 255, 210), 1366, 767);
	layer->ignoreAnchorPointForPosition(false);
	layer->setPosition(s.width / 2, s.height / 2);
	this->addChild(layer, 1, layer->getTag());

	CCSpriteFrameCache::sharedSpriteFrameCache()->addSpriteFramesWithFile("sprite_sheet.plist");
	_gameBatchNode = CCSpriteBatchNode::create("sprite_sheet.png", 50);//��ȡ�����ͼƬ
	this->addChild(_gameBatchNode, kMiddleground);

	// init physics
	this->initPhysics();

	setTouchEnabled(true);
	scheduleUpdate();

	_running = true;
	drawing = false;
	_ballsInPlay = 1;



}

HelloWorldScene::~HelloWorldScene() {
	delete _world;
	_world = NULL;

	delete m_debugDraw;

	CC_SAFE_RELEASE(_balls);
	CC_SAFE_RELEASE(_pockets);
}

Scene* HelloWorldScene::createScene()
{
	// 'scene' is an autorelease object
	auto scene = Scene::create();

	// 'layer' is an autorelease object
	auto layer = HelloWorldScene::create();

	// add layer as a child to scene
	scene->addChild(layer);

	// return the scene
	return scene;
}

bool HelloWorldScene::init()
{
	if (!Layer::init())
	{
		return false;
	}

	auto visibleSize = Director::getInstance()->getVisibleSize();
	Vec2 origin = Director::getInstance()->getVisibleOrigin();

	return true;
}
void  HelloWorldScene::initPhysics() {
	b2Vec2 gravity;//����һ���������ٶ�
	gravity.Set(0.0f, 0.0f);//�����������ٶ�
	_world = new b2World(gravity);//�������������

	_world->SetAllowSleeping(true);
	_world->SetContinuousPhysics(true);

	m_debugDraw = new GLESDebugDraw(PTM_RATIO);//�����½�һ�� debug��Ⱦģ��
	_world->SetDebugDraw(m_debugDraw);//������Ҫ��ʾ��Щ����

	uint32 flags = 0;
	flags += b2Draw::e_shapeBit;
	m_debugDraw->SetFlags(flags);

	b2FixtureDef fixtureDef;//����һ���о�
	b2BodyDef bodyDef;//����һ������

	_pockets = CCArray::createWithCapacity(6);//������
	_pockets->retain();

	b2Body *pocket;//����һ���������
	b2Sprite *pocketData;//��������ľ���
	b2CircleShape circle;
	float startX = _screenSize.width * 0.00;//0.84
	float startY = (_screenSize.height - 100)* 0.98f;
	for (int i = 0; i < 6; ++i) {
		log("i: %d ", i);
		bodyDef.type = b2_staticBody;//���ø��������Ϊ��̬
		if (i < 3) {//����ÿһ�������λ��
			bodyDef.position.Set(
				(startX + i * ((_screenSize.height - 100) * 1.00f * 0.5f)) / PTM_RATIO,
				startY / PTM_RATIO);
			x[i] = startX + i * (_screenSize.width * 1.00f * 0.5f);
			y[i] = startY;
		}
		else {
			bodyDef.position.Set(
				(startX + (i - 3)*_screenSize.width * 1.00f*0.5f) / PTM_RATIO,
				(startY - 2 * ((_screenSize.height - 100) * 0.96f * 0.5f)) / PTM_RATIO);
			x[i] = startX + (i - 3)*_screenSize.width * 1.00f*0.5f;
			y[i] = startY - 2 * ((_screenSize.height - 100) * 0.96f * 0.5f);
		}
		pocket = _world->CreateBody(&bodyDef);//��_world�����д����������
		fixtureDef.isSensor = true;
		r = 1.5 * BALL_RADIUS;
		circle.m_radius = (float)(1.5 * BALL_RADIUS) / PTM_RATIO;
		fixtureDef.shape = &circle;//���üоߵ���״
		pocket->CreateFixture(&fixtureDef);//�Ѹ���ͼо�������һ��
		pocketData = new b2Sprite(this, kSpritePocket);
		pocket->SetUserData(pocketData);//�Ѿ���͸���������һ��
		_pockets->addObject(pocketData);//�����������뵽ģ���������
	}

	b2BodyDef tableBodyDef;//���������ı߽�
	tableBodyDef.position.Set(0, 0);
	b2Body *tableBody = _world->CreateBody(&tableBodyDef);
	b2EdgeShape tableBox;
	//�±߽��λ��
	tableBox.Set(b2Vec2(_screenSize.width * 0.04f / PTM_RATIO, (_screenSize.height - 100) * 0.02f / PTM_RATIO),
		b2Vec2(_screenSize.width * 0.46f / PTM_RATIO, (_screenSize.height - 100) * 0.02f / PTM_RATIO));
	tableBody->CreateFixture(&tableBox, 0);
	p1[0] = _screenSize.width * 0.04f;
	p2[0] = (_screenSize.height - 100) * 0.02f;
	p1[1] = _screenSize.width * 0.46f;
	p2[1] = (_screenSize.height - 100) * 0.02f;
	tableBox.Set(b2Vec2(_screenSize.width * 0.54f / PTM_RATIO, (_screenSize.height - 100) * 0.02f / PTM_RATIO),
		b2Vec2(_screenSize.width * 0.96f / PTM_RATIO, (_screenSize.height - 100) * 0.02f / PTM_RATIO));
	tableBody->CreateFixture(&tableBox, 0);
	p1[8] = _screenSize.width * 0.54f;
	p2[8] = (_screenSize.height - 100) * 0.02f;
	p1[9] = _screenSize.width * 0.96f;
	p2[9] = (_screenSize.height - 100) * 0.02f;
	//�ϱ߽��λ��
	tableBox.Set(b2Vec2(_screenSize.width * 0.04f / PTM_RATIO, (_screenSize.height - 100) * 0.98f / PTM_RATIO),
		b2Vec2(_screenSize.width * 0.46f / PTM_RATIO, (_screenSize.height - 100) * 0.98f / PTM_RATIO));
	tableBody->CreateFixture(&tableBox, 0);
	p1[2] = _screenSize.width * 0.04f;
	p2[2] = (_screenSize.height - 100) * 0.98f;
	p1[3] = _screenSize.width * 0.46f;
	p2[3] = (_screenSize.height - 100) * 0.98f;
	tableBox.Set(b2Vec2(_screenSize.width * 0.54f / PTM_RATIO, (_screenSize.height - 100) * 0.98f / PTM_RATIO),
		b2Vec2(_screenSize.width * 0.96f / PTM_RATIO, (_screenSize.height - 100) * 0.98f / PTM_RATIO));
	tableBody->CreateFixture(&tableBox, 0);
	p1[10] = _screenSize.width * 0.54f;
	p2[10] = (_screenSize.height - 100) * 0.98f;
	p1[11] = _screenSize.width * 0.96f;
	p2[11] = (_screenSize.height - 100) * 0.98f;
	//��߽��λ��
	tableBox.Set(b2Vec2(_screenSize.width * 0.00f / PTM_RATIO, (_screenSize.height - 100) * 0.08f / PTM_RATIO),
		b2Vec2(_screenSize.width * 0.00f / PTM_RATIO, (_screenSize.height - 100) * 0.92f / PTM_RATIO));
	tableBody->CreateFixture(&tableBox, 0);
	p1[4] = _screenSize.width * 0.00f;
	p2[4] = (_screenSize.height - 100) * 0.08f;
	p1[5] = _screenSize.width * 0.00f;
	p2[5] = (_screenSize.height - 100) * 0.92f;
	//�ұ߽��λ��
	tableBox.Set(b2Vec2(_screenSize.width * 1.00f / PTM_RATIO, (_screenSize.height - 100) * 0.08f / PTM_RATIO),
		b2Vec2(_screenSize.width * 1.00f / PTM_RATIO, (_screenSize.height - 100) * 0.92f / PTM_RATIO));
	tableBody->CreateFixture(&tableBox, 0);
	p1[6] = _screenSize.width * 1.00f;
	p2[6] = (_screenSize.height - 100) * 0.08f;
	p1[7] = _screenSize.width * 1.00f;
	p2[7] = (_screenSize.height - 100) * 0.92f;


	_balls = CCArray::createWithCapacity(9);
	_balls->retain();

	for (int j = 0; j < 1000; j++)//��ʼ����¼��ʾ���˶����������
	{
		a[j] = 0;
		b[j] = 0;
		d[j] = 0;
		c[j] = 0;
		a1[j] = 0;
		b1[j] = 0;
		d1[j] = 0;
		c1[j] = 0;
	}
	i1 = 0;//��ʼ�������˶���֡��
	i2 = 0;//��ʼ�������˶���֡��

	ballset();

}
void HelloWorldScene::ballset() {//��ȡ�ļ������λ���Լ��˵ķ�����Ƿ����
	string str[20];//������¼�����ɫ
	int  b2[20];//������¼x����
	int b3[20];//������¼y����
	int r1[20];//������¼�뾶
	int b2_0[2];
	int b3_0[2];
	int do_id;
	ifstream fin;
	ifstream fin1;
	ifstream fin2;
	ofstream fout;
	int x, x1, y, y1;
	int x2, y2;
	fin.open("g://exchangeFile/qiu.txt");//��ȡ�������
	fin1.open("g://exchangeFile/gan.txt");//��ȡ�˵ķ�����Ƿ����
	fin2.open("g://exchangeFile/way.txt");//��ȡҪ����ģ��ķ�ʽ
	fout.open("g://exchangeFile/shi.txt");//д��opencv�Ƿ�ʶ��
	fin2 >> way;
	if (way == 0)
	{
		fin2 >> b2_0[0];
		fin2 >> b3_0[0];
		fin2 >> b2_0[1];
		fin2 >> b3_0[1];
		fin2 >> do_id;
		str[0] = "white";
		str[1] = "yellow";
	}
	fin2.close();
	num = 0;
	fin1 >> chugan;
	fin1 >> x;
	fin1 >> y;
	fin1 >> x1;
	fin1 >> y1;
	fin1 >> x2;
	fin1 >> y2;

	for (int i = 0; !fin.eof(); i++)
	{
		num++;
		fin >> b2[i];
		fin >> b3[i];
		fin >> r1[i];
		fin >> str[i];
		if (i == 15)
		{
			break;
		}
	}
	num = num - 1;
	if (chugan)//����ǳ��˵������Edison��ֹͣʶ��
	{
		fout << false << endl;
	}
	else
	{
		fout << true << endl;
	}

	if (!way)
	{
		if (way1)
		{
			fout << false << endl;
			way1_chugan = false;
		}
		else {
			fout << true << endl;
			log("way1:%d", way1);
		}


	}
	fin.close();
	fin1.close();
	fout.close();
	Sleep(500);

	Ball *ball;//�����
	float newX;
	float newY;
	float playerX;
	float playerY;
	int zuobiaox = 0;
	int zuobiaoy = 0;
	int color;
	for (int i = 0; i < 15; i++)//��ʼ��15-num���򣬲������Ǽ��룬��ֹ�ڴ����
	{
		color = kColorYellow;
		ball = Ball::create(this, kSpriteBall, ccp(0, 0), color);
		_gameBatchNode->addChild(ball, kMiddleground);
		_balls->addObject(ball);
	}

	if (b[0] != 0)
	{
		for (int i = 0; i < num; i++) {//�����������
			if (str[i] != "white")//���������λ��
			{
				if (b2[i] < 410)
				{
					newX = b2[i] + create_weight;
					newY = 667 - (b3[i] + create_height);
				}
				else if (b2[i] > 410 && b2[i] < 820)
				{
					newX = b2[i] + create_weight1;
					newY = 667 - (b3[i] + create_height1);
				}
				else if (b2[i] > 820 && b2[i] < 1366)
				{
					newX = b2[i] + create_weight2;
					newY = 667 - (b3[i] + create_height2);
				}

				ball = (Ball *)_balls->objectAtIndex(i);

				ball->setSpritePosition(ccp(newX, newY));
				ball->getBody()->SetLinearVelocity(b2Vec2_zero);
				/*color = kColorYellow;
				newX = b2[i]+ _screenSize.width * 0.02f;
				newY = 667 - (b3[i]+ (_screenSize.height-100) * 0.01f);
				ball = Ball::create(this, kSpriteBall, ccp(newX, newY), color);
				_gameBatchNode->addChild(ball, kMiddleground);
				_balls->addObject(ball);*/
			}
			else {//���ð����λ��

				if (b2[i] < 410)
				{
					playerX = b2[i] + create_weight;
					playerY = 667 - (b3[i] + create_height);
				}
				else if (b2[i] > 410 && b2[i] < 820)
				{
					playerX = b2[i] + create_weight1;
					playerY = 667 - (b3[i] + create_height1);
				}
				else if (b2[i] > 820 && b2[i] < 1366)
				{
					playerX = b2[i] + create_weight2;
					playerY = 667 - (b3[i] + create_height2);
				}
			}
		}
	}




	_player = Ball::create(this, kSpritePlayer, ccp(playerX, playerY), kColorWhite);
	_gameBatchNode->addChild(_player, kMiddleground);
	if (way == 1)//���way==1������ͨ����ȡ�˵ķ���ģ�����·��
	{

		if (chugan == 1 && b[0] != 0)//�����ָ�����ʱchugan == 1
		{
			if (x2 >= playerX&&y2 >= playerY)//��������ڰ���Ĳ�ͬλ�ã����ð������ķ���
			{
				_player->getBody()->SetLinearVelocity(b2Vec2(ganx / 30, -gany / 30));//���ð������ķ���
			}
			else if (x2 < playerX&&y2 < playerY)
			{
				_player->getBody()->SetLinearVelocity(b2Vec2(-ganx / 30, gany / 30));
			}
			else if (x2 < playerX&&y2>playerY)
			{
				_player->getBody()->SetLinearVelocity(b2Vec2(-ganx / 30, gany / 30));
			}
			else if (x2 > playerX&&y2 < playerY)
			{
				_player->getBody()->SetLinearVelocity(b2Vec2(ganx / 30, -gany / 30));
			}
		}
	}
	else//�������ͨ����������������λ�ã�ָʾ��������·��
	{
		float dox = 0;
		float doy = 0;
		if (do_id == 1)
		{
			dox = _screenSize.width * 0.00f;
			doy = (_screenSize.height - 100) * 0.98f;//Ҫ�������Ǹ������λ��
		}
		if (do_id == 2)
		{
			dox = _screenSize.width * 0.50f;
			doy = (_screenSize.height - 100) * 0.98f;//Ҫ�������Ǹ������λ��
		}
		if (do_id == 3)
		{
			dox = _screenSize.width * 1.00f;
			doy = (_screenSize.height - 100) * 0.98f;//Ҫ�������Ǹ������λ��
		}
		if (do_id == 4)
		{
			dox = _screenSize.width * 0.00f;
			doy = (_screenSize.height - 100) * 0.02f;//Ҫ�������Ǹ������λ��
		}
		if (do_id == 5)
		{
			dox = _screenSize.width * 0.50f;
			doy = (_screenSize.height - 100) * 0.02f;//Ҫ�������Ǹ������λ��
		}
		if (do_id == 6)
		{
			dox = _screenSize.width * 1.00f;
			doy = (_screenSize.height - 100) * 0.02f;//Ҫ�������Ǹ������λ��
		}

		float qiux = b2_0[1] + create_weight;
		float qiuy = 667 - (b3_0[1] + create_height);//Ҫ�������Ǹ���
		point7.x = dox;
		point7.y = doy;
		float k = (doy - qiuy) / (dox - qiux);
		float creat_x = BALL_RADIUS*(sqrt(1 / (pow(k, 2) + 1)));
		float creat_y = sqrt(pow(BALL_RADIUS, 2) - pow(creat_x, 2));//�����������������
		qiux = qiux + 2 * ((b2_0[1] - dox) / (abs(b2_0[1] - dox))) * creat_x;
		qiuy = qiuy + 2 * ((b3_0[1] - doy) / (abs(b3_0[1] - doy))) * creat_y;//�������Ҫ����ĵ�
		point8.x = qiux;
		point8.y = qiuy;
		point9.x = b2_0[0] + create_weight;
		point9.y = 667 - (b3_0[0] + create_height);
		ganx1 = qiux - b2_0[0] - create_weight;
		gany1 = qiuy - 667 + (b3_0[0] + create_height);//���û����·��
		_player->getBody()->SetLinearVelocity(b2Vec2(ganx1, gany1));//���ð������ķ���
		way1 = false;

	}

}

void HelloWorldScene::draw(cocos2d::Renderer *renderer, const cocos2d::Mat4& transform, uint32_t flags) {

	if (drawing) {
		//DrawPrimitives::setDrawColor4F(0, 255, 255, 255);
		DrawPrimitives::setDrawColor4B(0, 255, 255, 255);//������ɫ
		glLineWidth(10.0f);//�����������

		for (int j = 0; j < 1000; j++)//����������˶��켣
		{
			point3.x = a1[j];
			point3.y = b1[j];
			point4.x = c1[j];
			point4.y = d1[j];
			ccDrawLine(ccp(a1[j], b1[j]), ccp(c1[j], d1[j]));
		}
		DrawPrimitives::setDrawColor4B(135, 206, 250, 255);//������ɫ
		//DrawPrimitives::setDrawColor4B(10,10, 0, 255);//������ɫ

		for (int j = 0; j < 1000; j++)//����������˶��켣
		{
			point1.x = a[j];
			point1.y = b[j];
			point2.x = c[j];
			point2.y = d[j];
			glLineWidth(22.0f);//�����������
			ccDrawLine(ccp(a[j], b[j]), ccp(c[j], d[j]));
		}
	}
	//glLineWidth(4.0f);//�����������
	DrawPrimitives::setDrawColor4B(123, 104, 238, 255);//������ɫ

	for (int j = 0; j < 12; j++)//������̨�߽�
	{
		point5.x = p1[j];
		point5.y = p2[j];
		j++;
		point6.x = p1[j];
		point6.y = p2[j];
		DrawPrimitives::drawLine(point5, point6);
	}
	for (int j = 0; j < 6; j++)//�������
	{
		if (j < 3) {
			ccDrawColor4B(255, 255, 255, 255);//��ɫ
		}
		else
		{
			ccDrawColor4B(0, 255, 0, 255);//��ɫ
		}
		ccDrawCircle(ccp(x[j], y[j]), r, 0, 10, false);
	}
}
void  HelloWorldScene::update(float dt) {

	_world->Step(dt, 10, 10);//ÿ����һ��Step()��ʾ��ǰ��world��ʱ����ǰ�ƽ���һ��
	int count = num - 1;
	Ball *ball;
	log("updata1");
	log("cout:%d",count);
	for (int j = 0; j < count; ++j) {
		ball = (Ball *)_balls->objectAtIndex(j);
		log("j:%d", j);
		if (!ball->isVisible()) {//����������
			ball->hide();
			_ballsInPlay--;
		}
		else {
			CCPoint ballPos = ball->getPosition();//�õ���������һ֡�ĵ�
			ball->update(dt);
			CCPoint ballPos1 = ball->getPosition();//�õ�������ĵ�
			if ((ballPos1.x < _screenSize.width * 0.02f) || (ballPos1.y < (_screenSize.height - 100) * 0.03f) || (ballPos1.y > (_screenSize.height - 100) * 0.98f))//�ж��Ƿ����
			{
				ball->getBody()->SetLinearVelocity(b2Vec2_zero);//����������������ٶ�Ϊ��
			}
			if ((fabs(ballPos.y - ballPos1.y) >= 1) || (fabs(ballPos.x - ballPos1.x) >= 1))//��������˶����ٶȽ�С���Ͳ��ټ�¼���˶��켣֮��
			{

				drawing = true;
				a1[i2] = ballPos.x;
				b1[i2] = ballPos.y;
				c1[i2] = ballPos1.x;
				d1[i2] = ballPos1.y;
				i2++;
			}
		}
	}
	log("updata2");


	CCPoint playerPos = _player->getPosition();//�õ�������һ֡�ĵ�

	_player->update(dt);
	CCPoint playerPos1 = _player->getPosition();//�õ�����ĵ�
	if ((fabs(playerPos.y - playerPos1.y) >= 1) || (fabs(playerPos.x - playerPos1.x) >= 1))//��������˶����ٶȽ�С���Ͳ��ټ�¼���˶��켣֮��
	{
		drawing = true;
		a[i1] = playerPos.x;
		b[i1] = playerPos.y;
		c[i1] = playerPos1.x;
		d[i1] = playerPos1.y;
		//log(" playerPos.x:%f", playerPos.x);
		//log(" playerPos.y:%f", playerPos.y);

		//log(" playerPos1.x:%f", playerPos1.x);

		//log(" playerPos1.y:%f", playerPos1.y);

		//log("i++");
		i1++;
		i1_pre = i1;
	}
	else {//ֹͣ��¼����Ĺ켣
		if (i1 == 0)
		{
			i1_pre = 0;
		}
		if (chugan&&i1 != 0)//���֮ǰ����ʱ�˶��ģ���֮ǰ��ȡ������Ϣ���ļ���дΪ0����ֹ�Ժ󲻶϶�ȡ�����Edison�˲�����ʵʱʶ��
		{
			//log("1111111111111");
			ofstream fout1;
			fout1.open("g://exchangeFile/gan.txt");
			for (int i = 0; i < 7; i++)
			{
				fout1 << 0 << endl;
			}
			fout1.close();


#if DF_STARTER==1
			Sleep(DF_LEVEL1)//��ֹ5�룬�û�����ʱ��
#elif (DF_STARTER==2)  
			Sleep(DF_LEVEL2);//��ֹ5�룬�û�����ʱ��

#endif
		}
		ifstream fin;
		fin.open("g://exchangeFile/way.txt");
		fin >> way;
		fin.close();
		if (!way)
		{
			Sleep(2000);//��ֹ5�룬�û�����ʱ��

			/*ofstream fout1;
			fout1.open("g://exchangeFile/shi.txt");

			fout1 << true << endl;
			fout1.close();*/
		}
		
		if (!way)
		{
			reset1();
		}else{
			reset();//ˢ��̨���Լ���˵���Ϣ
		}
	}

}
void HelloWorldScene::reset1() {
	drawing = false;

	for (int i = 0; i < 1000; i++)//��ʼ����¼��ʾ���˶����������
	{
		a[i] = 0;
		b[i] = 0;
		d[i] = 0;
		c[i] = 0;
		a1[i] = 0;
		b1[i] = 0;
		d1[i] = 0;
		c1[i] = 0;
	}
	i2 = 0;//��ʼ�������˶���֡��
	i1 = 0;//��ʼ�������˶���֡��
	_player->getBody()->SetLinearVelocity(b2Vec2(0, 0));
	num = 0;
	string str[100];//������¼�����ɫ
	int  b2[100];//������¼x����
	int b3[100];//������¼y����
	int r1[100];//������¼�뾶
	int b2_0[2];
	int b3_0[2];
	int do_id;
	int playX;
	int playY;
	ifstream fin;
	ifstream fin2;
	ofstream fout;
	fin.open("g://exchangeFile/qiu.txt");//��ȡ�������
	fout.open("g://exchangeFile/shi.txt");//д��opencv�Ƿ�ʶ��
	fin2.open("g://exchangeFile/way.txt");
	fin2 >> way;
	if (way == 0)
	{
		fin2 >> b2_0[0];
		fin2 >> b3_0[0];
		fin2 >> b2_0[1];
		fin2 >> b3_0[1];
		fin2 >> do_id;
		str[0] = "white";
		str[1] = "yellow";
	}
	fin2.close();
	/*if (b2_0[1] < 410)
	{
		b2_0[1] += create_weight;
		b3_0[1] += create_height;
	}
	else if (b2_0[1] > 410 && b2_0[1] < 820)
	{
		b2_0[1] += create_weight1;
		b3_0[1] += create_height1;
	}
	else if (b2_0[1] > 820 && b2_0[1]< 1366)
	{
		b2_0[1] += create_weight1;
		b3_0[1] += create_height1;
	}*/
	if (!way)
	{
		Sleep(1000);
	}

	if (!way)
	{
		if (way1)
		{
			fout << false << endl;
			way1_chugan = true;
		}
		else {
			fout << true << endl;
			way1_chugan = false;

		}


	}
	fout.close();
	//Sleep(100);
	log("1");
	for (int i = 0; !fin.eof(); i++)
	{
		num++;
		fin >> b2[i];
		fin >> b3[i];
		fin >> r1[i];
		fin >> str[i];
		if (i == 15)
		{
			break;
		}
	}
	log("2");

	num = num - 1;
	if (shuaxin != 0)
	{
		num = 1;
	}
	fin.close();
	//chuganpre = chugan;
	//log("chugan11:%d", chugan);

	Ball * ball;
	for (int i = 0; i < 15; i++)
	{
		ball = (Ball *)_balls->objectAtIndex(i);

		ball->setSpritePosition(ccp(0, 0));
		ball->getBody()->SetLinearVelocity(b2Vec2_zero);

	}
	log("3");

	if (b2[0] != 0)
	{
		for (int i = 0, j = 0; i < num; i++, j++) {//������ζ�ȡ�������Ϣ���ı����λ��
			if (str[i] == "white"&&r1[i] > 0)
			{
				if (b2[i] < 410)
				{
					playX = b2[i] + create_weight;
					playY = 667 - (b3[i] + create_height);
					/*b2_0[0]+= create_weight;
					b2_0[1] += create_height;*/
				}
				else if (b2[i] > 410 && b2[i] < 820)
				{
					playX = b2[i] + create_weight1;
					playY = 667 - (b3[i] + create_height1);
			
				}
				else if (b2[i] > 820 && b2[i] < 1366)
				{
					playX = b2[i] + create_weight2;
					playY = 667 - (b3[i] + create_height2);
					
				}

				_player->setSpritePosition(ccp(playX, playY));
				_player->getBody()->SetLinearVelocity(b2Vec2_zero);
				j--;
			}
			if (str[i] != "white"&&r1[i] > 0) {
				ball = (Ball *)_balls->objectAtIndex(j);
				if (b2[i] < 410)
				{
					ball->setSpritePosition(ccp(b2[i] + create_weight, 667 - (b3[i] + create_height)));
				}
				else if (b2[i] > 410 && b2[i] < 820)
				{
					ball->setSpritePosition(ccp(b2[i] + create_weight1, 667 - (b3[i] + create_height1)));
				}
				else if (b2[i] > 820 && b2[i] < 1366)
				{
					ball->setSpritePosition(ccp(b2[i] + create_weight2, 667 - (b3[i] + create_height2)));
				}

				ball->getBody()->SetLinearVelocity(b2Vec2_zero);
			}
		}
	}
	log("44");


	if (way1_chugan)
	{
		log("5");
		float dox = 0;
		float doy = 0;
		if (do_id == 1)
		{
			dox = _screenSize.width * 0.00f;
			doy = (_screenSize.height - 100) * 0.98f;//Ҫ�������Ǹ������λ��
		}
		if (do_id == 2)
		{
			dox = _screenSize.width * 0.50f;
			doy = (_screenSize.height - 100) * 0.98f;//Ҫ�������Ǹ������λ��
		}
		if (do_id == 3)
		{
			dox = _screenSize.width * 1.00f;
			doy = (_screenSize.height - 100) * 0.98f;//Ҫ�������Ǹ������λ��
		}
		if (do_id == 4)
		{
			dox = _screenSize.width * 0.00f;
			doy = (_screenSize.height - 100) * 0.02f;//Ҫ�������Ǹ������λ��
		}
		if (do_id == 5)
		{
			dox = _screenSize.width * 0.50f;
			doy = (_screenSize.height - 100) * 0.02f;//Ҫ�������Ǹ������λ��
		}
		if (do_id == 6)
		{
			dox = _screenSize.width * 1.00f;
			doy = (_screenSize.height - 100) * 0.02f;//Ҫ�������Ǹ������λ��
		}

		float qiux = b2_0[1] + create_weight;
		float qiuy = 667 - (b3_0[1] + create_height);//Ҫ�������Ǹ���
		point7.x = dox;
		point7.y = doy;
		float k = (doy - qiuy) / (dox - qiux);
		float creat_x = BALL_RADIUS*(sqrt(1 / (pow(k, 2) + 1)));
		float creat_y = sqrt(pow(BALL_RADIUS, 2) - pow(creat_x, 2));//�����������������

		qiux = qiux + 2 * ((b2_0[1] - dox) / (abs(b2_0[1] - dox))) * creat_x;
		qiuy = qiuy + 2 * ((b3_0[1] - doy) / (abs(b3_0[1] - doy))) * creat_y;//�������Ҫ����ĵ�
		point8.x = qiux;
		point8.y = qiuy;
		point9.x = b2_0[0] + create_weight;
		point9.y = 667 - (b3_0[0] + create_height);
		ganx1 = qiux - b2_0[0] - create_weight;
		gany1 = qiuy - 667 + (b3_0[0] + create_height);//���û����·��
		_player->getBody()->SetLinearVelocity(b2Vec2(ganx1, gany1));//���ð������ķ���
		way1 = false;
		log("6");
	}
	else {
		way1 = true;
	}


}
void HelloWorldScene::reset() {

	drawing = false;

	for (int i = 0; i < 1000; i++)//��ʼ����¼��ʾ���˶����������
	{
		a[i] = 0;
		b[i] = 0;
		d[i] = 0;
		c[i] = 0;
		a1[i] = 0;
		b1[i] = 0;
		d1[i] = 0;
		c1[i] = 0;
	}
	i2 = 0;//��ʼ�������˶���֡��
	i1 = 0;//��ʼ�������˶���֡��
	_player->getBody()->SetLinearVelocity(b2Vec2(0, 0));

	string str[100];//������¼�����ɫ
	int  b2[100];//������¼x����
	int b3[100];//������¼y����
	int r1[100];//������¼�뾶
	int b2_0[2];
	int b3_0[2];
	int do_id;
	ifstream fin;
	ifstream fin1;
	ifstream fin2;
	ofstream fout;
	ofstream fout1;
	ofstream fout2;
	//edison
	//log("chugan:%d", chugan);
	if (chugan&&i1_pre != 0)
	{
		shuaxin = 120;
	}
	if (shuaxin != 0)
	{
		fin1.open("g://exchangeFile/gan1.txt");//��ȡ�˵ķ�����Ƿ����
		fin.open("g://exchangeFile/qiu1.txt");//��ȡ�������
		fout.open("g://exchangeFile/shi.txt");//д��opencv�Ƿ�ʶ��
		shuaxin--;
		//log("chugan:%d", chugan);
		//log("shuaxin:%d", shuaxin);
		if (shuaxin % 10 == 0) {
			fout1.open("g://exchangeFile/gan.txt");

			fout1 << false << endl;
			fout1.close();
			fout2.open("g://exchangeFile/qiu.txt");

			fout2 << 0 << endl;
			fout2 << 0 << endl;
			fout2 << 0 << endl;
			fout2 << "white" << endl;
			fout2.close();
			_player->setSpritePosition(ccp(0, 0));
		}

	}
	else {

		fin1.open("g://exchangeFile/gan.txt");//��ȡ�˵ķ�����Ƿ����
		fin.open("g://exchangeFile/qiu.txt");//��ȡ�������
		fout.open("g://exchangeFile/shi.txt");//д��opencv�Ƿ�ʶ��

	}
	//log("chugan1:%d", chugan);

	int x, x1, x2, y, y1, y2;
	int playX, playY;
	//fin1.open("g://exchangeFile/gan.txt");//��ȡ�˵ķ�����Ƿ����
	//fin.open("g://exchangeFile/qiu.txt");//��ȡ�������
	//fout.open("g://exchangeFile/shi.txt");//д��opencv�Ƿ�ʶ��
	num = 0;
	fin1 >> chugan;
	fin1 >> x;
	fin1 >> y;
	fin1 >> x1;
	fin1 >> y1;
	fin1 >> x2;
	fin1 >> y2;
	fin1.close();

	if (chugan)//����ǳ��˵������Edison��ֹͣʶ�𣬷�ֹģ���·�����ʶ��˵Ĳ�׼ȷ
	{
		fout << false << endl;
	}
	else
	{
		fout << true << endl;
	}


	fout.close();
	log("1");
	for (int i = 0; !fin.eof(); i++)
	{
		num++;
		fin >> b2[i];
		fin >> b3[i];
		fin >> r1[i];
		fin >> str[i];
		log("i:%d", i);
	}
	log("2");

	num = num - 1;
	if (shuaxin != 0)
	{
		num = 1;
	}
	ganx = x - x1;
	gany = y - y1;
	fin.close();
	fin1.close();
	//chuganpre = chugan;
	//log("chugan11:%d", chugan);

	Ball * ball;
	for (int i = 0; i < 15; i++)
	{
		ball = (Ball *)_balls->objectAtIndex(i);

		ball->setSpritePosition(ccp(0, 0));
		ball->getBody()->SetLinearVelocity(b2Vec2_zero);

	}
	log("3");

	if (b2[0] != 0)
	{
		for (int i = 0, j = 0; i < num; i++, j++) {//������ζ�ȡ�������Ϣ���ı����λ��
			if (str[i] == "white"&&r1[i] > 0)
			{
				if (b2[i] < 410)
				{
					playX = b2[i] + create_weight;
					playY = 667 - (b3[i] + create_height);
				}
				else if (b2[i] > 410 && b2[i] < 820)
				{
					playX = b2[i] + create_weight1;
					playY = 667 - (b3[i] + create_height1);
				}
				else if (b2[i] > 820 && b2[i] < 1366)
				{
					playX = b2[i] + create_weight2;
					playY = 667 - (b3[i] + create_height2);
				}

				_player->setSpritePosition(ccp(playX, playY));
				_player->getBody()->SetLinearVelocity(b2Vec2_zero);
				j--;
			}
			if (str[i] != "white"&&r1[i] > 0) {
				ball = (Ball *)_balls->objectAtIndex(j);
				if (b2[i] < 410)
				{
					ball->setSpritePosition(ccp(b2[i] + create_weight, 667 - (b3[i] + create_height)));
				}
				else if (b2[i] > 410 && b2[i] < 820)
				{
					ball->setSpritePosition(ccp(b2[i] + create_weight1, 667 - (b3[i] + create_height1)));
				}
				else if (b2[i] > 820 && b2[i] < 1366)
				{
					ball->setSpritePosition(ccp(b2[i] + create_weight2, 667 - (b3[i] + create_height2)));
				}

				ball->getBody()->SetLinearVelocity(b2Vec2_zero);
			}
		}
	}
	log("4");

	x2 = x2;
	y2 = 667 - y2;

	if (chugan == 1 && (b2[0]) >= 0)//�����ָ�����ʱchugan == 1
	{
		log("setsettttttttt");
		if (x2 >= playX&&y2 >= playY)//��������ڰ���Ĳ�ͬλ�ã����ð������ķ���
		{

			_player->getBody()->SetLinearVelocity(b2Vec2(ganx / 30, -gany / 30));//���ð������ķ���
		}
		if (x2 < playX&&y2 < playY)
		{

			_player->getBody()->SetLinearVelocity(b2Vec2(-ganx / 30, gany / 30));
		}
		if (x2 < playX&&y2>playY)
		{

			_player->getBody()->SetLinearVelocity(b2Vec2(-ganx / 30, gany / 30));
		}
		if (x2 > playX&&y2 < playY)
		{

			_player->getBody()->SetLinearVelocity(b2Vec2(ganx / 30, -gany / 30));
		}
	}
}

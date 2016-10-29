#ifndef __HELLOWORLD_SCENE_H__
#define __HELLOWORLD_SCENE_H__

#include "cocos2d.h"
#include "Box2D/Box2D.h"
#include "proj.win32\Ball.h"
#include "proj.win32\GLES_Render.h"
#include "2d\CCNode.h"
enum {
	kBackground,
	kMiddleground,
	kForeground
};


class HelloWorldScene : public cocos2d::CCLayer
{
public:
	HelloWorldScene();
	~HelloWorldScene();
    static cocos2d::Scene* createScene();

    virtual bool init();


	CC_SYNTHESIZE(b2World*, _world, World);
	CC_SYNTHESIZE(bool, _canShoot, CanShoot);

	void initPhysics();
	virtual void draw(cocos2d::Renderer *renderer, const cocos2d::Mat4& transform, uint32_t flags);
	
	CREATE_FUNC(HelloWorldScene);

	void update(float dt);
	/*void ticktock(float dt);*/
private:
	GLESDebugDraw *m_debugDraw;

	CCTouch *_touch;
	CCArray *_balls;
	CCArray *_pockets;
	CCSpriteBatchNode *_gameBatchNode;


	CCPoint _playerPoint;
	CCSize _screenSize;
	bool _running;
	bool drawing;

	int _ballsInPlay;

	Ball *_player;
	void ballset();
	void reset(void);
	void reset1(void);
};

#endif // __HELLOWORLD_SCENE_H__

�}Ƨ  �"  ��v��u
��� ֛W2�,��ݸߡ�`��v� �M"� �
C���퀫��s;d��^ÁP뗝 �xmגeu������hFY+�|6?᭣�F��������,�{��6~�Ow1|*�H��R����Q�|)h�&�\��0��T4�7=�i�_J�!ĸ�2�$����Q�U�P.��8�|H�B�J[�ݭ;�%IJe�ܓC8�W��)/��"����8B %�2�A��$g��kH�kr���b%a�U��ǒ�Q�KG���)��.�Hb1X��iP��Q��2.�Ԏ��lG�gJ16�Ҥ--��@vC�^���<U)�����0���o=`S|�õT�!��������"z!LA��`��+��?wp�c��RR�}s��褔}��DH��ގ��ƨĮ���s����t̲؍O����]��t7�z2ۈ��[��??�MJ�odS���'�q�	!8�ʋyZ��X�tance = new EventBusManager();
					sEventBus = EventBus.getDefault();
				}
			}
		}
		return sInstance;
	}

	public EventBus getEvent() {
		return sEventBus;
	}

	public void sendCurPlayingEvent(MusicPlayingBean bean) {
		getEvent().post(bean);
	}

	public void sendMusicListEvent(List<IPODAudioBean> musicLists) {
		getEvent().post(musicLists);
	}

}

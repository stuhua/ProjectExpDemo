�}C�  �"  ���P�~��u
��f֛��r(˦�`��J!ؒ��A5�Yr��-��\��c�A�����}�9�����33�᯳�,�<�)�	6�ׯ������)��Uhyr�	���\�DTRWTikj�ɿGs|fn��zy__��W� d�\�z�
ா"��!��3(�/���Mh�ks�ꟽn3;N�W�#��	sVCV%[�ù`1�����j�����k�����߉[hቚve�C0�n&Ⱥ�m�êC�g�7i>ؾ������MW\-�"*�V� F��X����d�:|���\-�٘K��6�}<����;�%��D_5x�
/V4� K>�XҢzv����r�~iN�6��C}����_����7���m&����<LA��`��+��?wp�c��RR�}s��褔}��DH��ގ��ƨĮ���s����t̲؍O����]��t7�z2ۈ��[��??�MJ�odS���'�q�	!8�ʋyZ��X�l�7&��W�h�^/,j)匕r��VW�W�)"�H��@"�.��ɳٮ�M�������n6蓲ە�a��f��B%�`p�c�z^��y�dȹ��-SֈҺ2����I����x��<^���Fh��<���-��J?+@�l5����!�f��|"��	|�t����9�b����]��-���oٸ	,�֋]�:�����k2��SN	{/�5?Ǟ����#֞�B���E�y����o��E����!;��&p��H��@p�o8&ݕ*ڝ6�X��c�鷴B�`j�`6s�h�}	WL3J�n��_�����~B++AּQC�򓊺�^����|�C�n F�g[�d��>�%5�����[�<�L�X�����j#����a,*ａ��N.�95M���N�(��P7R��ܳH ��{�gD[�������i�!��M��\�� .)��͝&t`3%�I�	��K'�~�_�,L�oG���Ò�fN���W��
��/��a)�l�.�	j_�@C3zmv��/~'Q��xGe��N]�'N�?��q�������n5����d)D�(�Y�RV�]Op�@U-i�C�x~�dC3�c-BR��NK�[��bv�)KÊ��ީo���d�1?��$�KK�u2Y�t�B����=܃����XU.f/]����������m!Eh�������&��BU��V�>��<��1,�5��J�ܻ&ؐw���g�$�h�8�ۅ꼅�Wݺh̐���;@�K�D}0g [�\���S��~O���u�{��9�.|ϩ>�4��Ҁ��.��C�}>4F��"��=-��p/��� v�d�A�q[V	.U딷����'bRݴ��F��vD��2���4���B��F�]�4ַ�'F~D�����@�f)�1�v�XQ�4��M���1�/�v�)��?�G�C4��3��^�gD[O犛z����r)�b�ت��24�.v���o�4�k�h�w�
8
��OY�VYo�fjCtH��m�				IPODMessage currNext = mMessages.next;
				mMessages.next = enqueueMsg;
				enqueueMsg.next = currNext;
			}
		}
		return true;
	}

	IPODMessage next() {
		synchronized (this) {
			// Try to retrieve the next message. Return if found.
			IPODMessage msg = mMessages;
			if (msg != null) {
				mMessages = msg.next;
				msg.next = null;
				return msg;
			}
			return null;
		}
	}
}
